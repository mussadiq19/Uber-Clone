const AppConfig = {
    websocket: {
        baseUrl: 'http://localhost:8080',
        endpoint: '/ws',
        topicPrefix: '/topic',
        appPrefix: '/app',
        reconnectAttempts: 5,
        reconnectDelay: 1000,
        reconnectMaxDelay: 30000,
    },
    api: {
        booking: 'http://localhost:6965',
        location: 'http://localhost:6965',
        auth: 'http://localhost:6965',
    }
};

if (typeof module !== 'undefined' && module.exports) module.exports = AppConfig;
