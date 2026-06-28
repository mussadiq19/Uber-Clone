/**
 * Driver Handler
 * - Connects to WebSocket and subscribes to /topic/rideRequest
 * - Receives RideRequestDto: { passengerId, driverIds, bookingId }
 * - Sends RideResponseDto:   { response, bookingId } to /app/rideResponse/{driverId}
 * - Updates driver location via LocationService POST /api/location/drivers
 */

const DriverHandler = {
    driverId: null,
    activeRequests: {},

    init(driverId) {
        this.driverId = driverId;
        this._updateConnectionUI('CONNECTING', 'Connecting to server...');

        WebSocketClient.connect(
            () => this._onConnected(),
            () => this._onDisconnected(),
            (err) => this._onError(err)
        );
    },

    _onConnected() {
        this._updateConnectionUI('CONNECTED', 'Connected! Listening for ride requests...');

        // Subscribe to ride requests broadcast
        WebSocketClient.subscribe('/topic/rideRequest', (rideRequest) => {
            console.log('New ride request:', rideRequest);
            this._handleRideRequest(rideRequest);
        });
    },

    _onDisconnected() {
        this._updateConnectionUI('DISCONNECTED', 'Disconnected. Attempting to reconnect...');
    },

    _onError(err) {
        this._updateConnectionUI('DISCONNECTED', 'Connection error. Retrying...');
    },

    _handleRideRequest(rideRequest) {
        // Only show if this driver is in the driverIds list (or list is empty = broadcast to all)
        const { bookingId, passengerId, driverIds } = rideRequest;
        if (driverIds && driverIds.length > 0 && !driverIds.includes(Number(this.driverId))) {
            return;
        }

        this.activeRequests[bookingId] = rideRequest;
        this._renderRequests();
    },

    acceptRide(bookingId) {
        WebSocketClient.sendRideResponse(this.driverId, bookingId, true);
        delete this.activeRequests[bookingId];
        this._renderRequests();
        this._showToast(`Accepted booking #${bookingId}`, 'success');
    },

    rejectRide(bookingId) {
        WebSocketClient.sendRideResponse(this.driverId, bookingId, false);
        delete this.activeRequests[bookingId];
        this._renderRequests();
        this._showToast(`Rejected booking #${bookingId}`, 'info');
    },

    async updateLocation(latitude, longitude) {
        try {
            await ApiClient.saveDriverLocation(this.driverId, latitude, longitude);
            this._showToast('Location updated', 'success');
        } catch (e) {
            this._showToast('Failed to update location', 'error');
        }
    },

    _renderRequests() {
        const container = document.getElementById('ride-requests-container');
        if (!container) return;

        const requests = Object.values(this.activeRequests);
        if (requests.length === 0) {
            container.innerHTML = '<p class="empty">No active ride requests. Waiting...</p>';
            return;
        }

        container.innerHTML = requests.map(r => `
            <div class="ride-card">
                <div class="ride-info">
                    <span class="label">Booking ID</span>
                    <span class="value">#${r.bookingId}</span>
                </div>
                <div class="ride-info">
                    <span class="label">Passenger ID</span>
                    <span class="value">${r.passengerId}</span>
                </div>
                <div class="ride-actions">
                    <button class="btn btn-accept" onclick="DriverHandler.acceptRide(${r.bookingId})">Accept</button>
                    <button class="btn btn-reject" onclick="DriverHandler.rejectRide(${r.bookingId})">Reject</button>
                </div>
            </div>
        `).join('');
    },

    _updateConnectionUI(status, message) {
        const statusEl = document.getElementById('connection-status');
        const statusTextEl = document.getElementById('connection-status-text');
        const messageEl = document.getElementById('connection-message');

        const cls = status === 'CONNECTED' ? 'connected' : 'disconnected';

        if (statusEl) {
            statusEl.textContent = status;
            statusEl.className = `status-badge ${cls}`;
        }
        if (statusTextEl) {
            statusTextEl.textContent = status;
            statusTextEl.className = `status-badge ${cls}`;
        }
        if (messageEl) messageEl.textContent = message;
    },

    _showToast(message, type = 'info') {
        const toast = document.createElement('div');
        toast.className = `toast toast-${type}`;
        toast.textContent = message;
        document.body.appendChild(toast);
        setTimeout(() => toast.remove(), 3000);
    }
};

if (typeof module !== 'undefined' && module.exports) module.exports = DriverHandler;
