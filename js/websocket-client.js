/**
 * WebSocket Client
 * SocketService (8080):
 *   Handshake:  /ws  (SockJS)
 *   Subscribe:  /topic/rideRequest   ← server pushes new ride requests to drivers
 *   Send:       /app/rideResponse/{driverId} ← driver accepts/rejects
 */

const WebSocketClient = {
    socket: null,
    stompClient: null,
    isConnected: false,
    reconnectAttempts: 0,
    reconnectTimer: null,
    subscriptions: {},

    connect(onConnect, onDisconnect, onError) {
        const url = `${AppConfig.websocket.baseUrl}${AppConfig.websocket.endpoint}`;
        console.log('Connecting to WebSocket:', url);

        this.socket = new SockJS(url);
        this.stompClient = Stomp.over(this.socket);
        this.stompClient.debug = null;

        this.stompClient.connect({}, (frame) => {
            console.log('WebSocket connected:', frame);
            this.isConnected = true;
            this.reconnectAttempts = 0;
            if (onConnect) onConnect(frame);
        }, (error) => {
            console.error('WebSocket error:', error);
            this.isConnected = false;
            if (onError) onError(error);
            this._scheduleReconnect(onConnect, onDisconnect, onError);
        });

        this.socket.onclose = () => {
            if (this.isConnected) {
                this.isConnected = false;
                if (onDisconnect) onDisconnect();
                this._scheduleReconnect(onConnect, onDisconnect, onError);
            }
        };
    },

    _scheduleReconnect(onConnect, onDisconnect, onError) {
        if (this.reconnectTimer) return;
        if (this.reconnectAttempts >= AppConfig.websocket.reconnectAttempts) {
            console.error('Max reconnection attempts reached');
            return;
        }
        this.reconnectAttempts++;
        const delay = Math.min(
            AppConfig.websocket.reconnectDelay * Math.pow(2, this.reconnectAttempts - 1),
            AppConfig.websocket.reconnectMaxDelay
        );
        console.log(`Reconnecting in ${delay}ms (attempt ${this.reconnectAttempts})`);
        this.reconnectTimer = setTimeout(() => {
            this.reconnectTimer = null;
            this.connect(onConnect, onDisconnect, onError);
        }, delay);
    },

    subscribe(topic, callback) {
        if (!this.isConnected || !this.stompClient) return null;
        const sub = this.stompClient.subscribe(topic, (message) => {
            try {
                callback(JSON.parse(message.body));
            } catch (e) {
                console.error('Error parsing message:', e);
            }
        });
        this.subscriptions[topic] = sub;
        return sub;
    },

    // Send driver's response to a ride request
    // destination: /app/rideResponse/{driverId}
    // body: { response: true/false, bookingId }
    sendRideResponse(driverId, bookingId, accepted) {
        if (!this.isConnected || !this.stompClient) return false;
        const dest = `${AppConfig.websocket.appPrefix}/rideResponse/${driverId}`;
        this.stompClient.send(dest, {}, JSON.stringify({
            response: accepted,
            bookingId: bookingId
        }));
        return true;
    },

    disconnect() {
        Object.keys(this.subscriptions).forEach(t => {
            this.subscriptions[t].unsubscribe();
        });
        this.subscriptions = {};
        if (this.reconnectTimer) {
            clearTimeout(this.reconnectTimer);
            this.reconnectTimer = null;
        }
        if (this.stompClient) {
            this.stompClient.disconnect();
            this.stompClient = null;
        }
        if (this.socket) {
            this.socket.close();
            this.socket = null;
        }
        this.isConnected = false;
    }
};

if (typeof module !== 'undefined' && module.exports) module.exports = WebSocketClient;
