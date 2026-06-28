/**
 * Passenger Handler
 * - Creates booking via POST /api/v1/booking
 *   Body: { passengerId, startLocation: { latitude, longitude }, endLocation: { latitude, longitude } }
 *   Response: { bookingId, bookingStatus, driver }
 *
 * - Updates booking via POST /api/v1/booking/{id}
 *   Body: { bookingStatus, driverId }
 *
 * - Gets nearby drivers via POST /api/location/nearby/drivers
 *   Body: { latitude, longitude }
 */

const PassengerHandler = {
    passengerId: null,
    activeBooking: null,

    init(passengerId) {
        this.passengerId = passengerId;
        this._renderActiveBooking();
    },

    async createBooking(startLat, startLng, endLat, endLng) {
        try {
            const res = await ApiClient.createBooking(
                Number(this.passengerId),
                parseFloat(startLat), parseFloat(startLng),
                parseFloat(endLat),   parseFloat(endLng)
            );

            if (res.success && res.data) {
                // Response: { bookingId, bookingStatus, driver }
                this.activeBooking = res.data;
                this._showToast(`Booking #${res.data.bookingId} created — Status: ${res.data.bookingStatus}`, 'success');
                this._renderActiveBooking();
            }
        } catch (err) {
            this._showToast(err.message || 'Failed to create booking', 'error');
        }
    },

    async cancelBooking(bookingId) {
        if (!confirm('Cancel this booking?')) return;
        try {
            const res = await ApiClient.updateBooking(bookingId, 'CANCELLED');
            if (res.success) {
                this.activeBooking = null;
                this._renderActiveBooking();
                this._showToast('Booking cancelled', 'info');
            }
        } catch (err) {
            this._showToast(err.message || 'Failed to cancel booking', 'error');
        }
    },

    async findNearbyDrivers(latitude, longitude) {
        try {
            const res = await ApiClient.getNearbyDrivers(parseFloat(latitude), parseFloat(longitude));
            if (res.success && res.data) {
                this._renderNearbyDrivers(res.data);
            }
        } catch (err) {
            this._showToast('Failed to fetch nearby drivers', 'error');
        }
    },

    _renderActiveBooking() {
        const container = document.getElementById('active-booking-container');
        if (!container) return;

        if (!this.activeBooking) {
            container.innerHTML = '<p class="empty">No active booking.</p>';
            return;
        }

        const b = this.activeBooking;
        const driver = b.driver || null;

        container.innerHTML = `
            <div class="booking-card">
                <div class="booking-row">
                    <span class="label">Booking ID</span>
                    <span class="value">#${b.bookingId}</span>
                </div>
                <div class="booking-row">
                    <span class="label">Status</span>
                    <span class="value status-badge ${b.bookingStatus?.toLowerCase()}">${b.bookingStatus}</span>
                </div>
                <div class="booking-row">
                    <span class="label">Driver</span>
                    <span class="value">${driver ? driver.name || `Driver #${driver.id}` : 'Not assigned yet'}</span>
                </div>
                ${b.bookingStatus === 'PENDING' || b.bookingStatus === 'CONFIRMED' ? `
                <div class="booking-actions">
                    <button class="btn btn-reject" onclick="PassengerHandler.cancelBooking(${b.bookingId})">Cancel Booking</button>
                </div>` : ''}
            </div>
        `;
    },

    _renderNearbyDrivers(drivers) {
        const container = document.getElementById('nearby-drivers-container');
        if (!container) return;

        if (!drivers || drivers.length === 0) {
            container.innerHTML = '<p class="empty">No drivers nearby.</p>';
            return;
        }

        container.innerHTML = `
            <table class="data-table">
                <thead><tr><th>Driver ID</th><th>Latitude</th><th>Longitude</th></tr></thead>
                <tbody>
                    ${drivers.map(d => `
                        <tr>
                            <td>${d.driverId}</td>
                            <td>${d.latitude}</td>
                            <td>${d.longitude}</td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        `;
    },

    _showToast(message, type = 'info') {
        const toast = document.createElement('div');
        toast.className = `toast toast-${type}`;
        toast.textContent = message;
        document.body.appendChild(toast);
        setTimeout(() => toast.remove(), 4000);
    }
};

if (typeof module !== 'undefined' && module.exports) module.exports = PassengerHandler;
