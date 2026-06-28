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
 */

const ApiClient = {

    async _post(baseUrl, path, body) {
        const url = `${baseUrl}${path}`;
        try {
            const res = await fetch(url, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
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
