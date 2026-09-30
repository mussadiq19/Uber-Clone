/**
 * API Client — matches actual backend endpoints exactly
 *
 * BookingService (8000):
 *   POST /api/v1/booking               → createBooking
 *   POST /api/v1/booking/{id}          → updateBooking
 *
 * LocationService (7777):
 *   POST /api/location/drivers         → saveDriverLocation
 *   POST /api/location/nearby/drivers  → getNearbyDrivers
 *
 * AuthService (config.js -> AppConfig.api.auth):
 *   POST /api/v1/auth/signup/passenger
 *   POST /api/v1/auth/signup/driver
 *   POST /api/v1/auth/signin           → { success, role, id }
 *   GET  /api/v1/auth/validate
 *
 * NOTE: credentials: 'include' is set on every request so the httpOnly
 * JwtToken cookie set by AuthService is sent along to every service.
 * Every backend's CORS config must allow credentials from this origin
 * (allowedOriginPatterns, not a literal "*", with allowCredentials(true)).
 */

const ApiClient = {

    async _post(baseUrl, path, body) {
        const url = `${baseUrl}${path}`;
        try {
            const res = await fetch(url, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include',
                body: JSON.stringify(body)
            });
            const data = await res.json().catch(() => null);
            if (!res.ok) throw { status: res.status, message: data?.message || `HTTP ${res.status}`, data };
            return { success: true, data };
        } catch (err) {
            console.error(`POST ${url} failed:`, err);
            throw err;
        }
    },

    async _get(baseUrl, path) {
        const url = `${baseUrl}${path}`;
        try {
            const res = await fetch(url, { method: 'GET', credentials: 'include' });
            const data = await res.json().catch(() => null);
            if (!res.ok) throw { status: res.status, message: data?.message, data };
            return { success: true, data };
        } catch (err) {
            console.error(`GET ${url} failed:`, err);
            throw err;
        }
    },

    // ── Auth Service ─────────────────────────────────────────────

    // POST /api/v1/auth/signin
    // Body: { email, password }
    // Returns: { success, role, id }  ← "id" requires the small AuthController
    // change discussed: including the passenger/driver id in AuthResponseDto.
    signIn(email, password) {
        return this._post(AppConfig.api.auth, '/api/v1/auth/signin', { email, password });
    },

    // POST /api/v1/auth/signup/passenger
    // Body: { email, name, phoneNumber, password }
    signUpPassenger(email, name, phoneNumber, password) {
        return this._post(AppConfig.api.auth, '/api/v1/auth/signup/passenger', {
            email, name, phoneNumber, password
        });
    },

    // POST /api/v1/auth/signup/driver
    // Body: { email, name, phoneNumber, password }
    signUpDriver(email, name, phoneNumber, password) {
        return this._post(AppConfig.api.auth, '/api/v1/auth/signup/driver', {
            email, name, phoneNumber, password
        });
    },

    validateSession() {
        return this._get(AppConfig.api.auth, '/api/v1/auth/validate');
    },

    // ── Booking Service ──────────────────────────────────────────

    // POST /api/v1/booking
    // Body: { passengerId, startLocation: { latitude, longitude }, endLocation: { latitude, longitude } }
    // Returns: { bookingId, bookingStatus, driver }
    createBooking(passengerId, startLat, startLng, endLat, endLng) {
        return this._post(AppConfig.api.booking, '/api/v1/booking', {
            passengerId,
            startLocation: { latitude: startLat, longitude: startLng },
            endLocation:   { latitude: endLat,   longitude: endLng   }
        });
    },

    // POST /api/v1/booking/{bookingId}
    // Body: { bookingStatus, driverId }
    // Returns: { bookingId, status, driver }
    updateBooking(bookingId, bookingStatus, driverId = null) {
        return this._post(AppConfig.api.booking, `/api/v1/booking/${bookingId}`, {
            bookingStatus,
            driverId
        });
    },
    getBooking(bookingId) {
        return this._get(AppConfig.api.booking, `/api/v1/booking/${bookingId}`);
    },

    // ── Location Service ─────────────────────────────────────────

    // POST /api/location/drivers
    // Body: { driverId, latitude, longitude }
    saveDriverLocation(driverId, latitude, longitude) {
        return this._post(AppConfig.api.location, '/api/location/drivers', {
            driverId, latitude, longitude
        });
    },

    // POST /api/location/nearby/drivers
    // Body: { latitude, longitude }
    // Returns: List<DriverLocationDto>
    getNearbyDrivers(latitude, longitude) {
        return this._post(AppConfig.api.location, '/api/location/nearby/drivers', {
            latitude, longitude
        });
    }
};

if (typeof module !== 'undefined' && module.exports) module.exports = ApiClient;
