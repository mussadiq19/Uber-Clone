/**
 * Auth Handler
 * - Toggles PASSENGER / DRIVER role and Sign in / Create account mode
 * - Calls ApiClient.signIn / ApiClient.signUp
 * - On success, redirects into index.html with the right role + id
 *
 * Expects ApiClient (api-client.js) to expose:
 *   signIn(email, password)                                -> { success, role, id }
 *   signUpPassenger(email, name, phoneNumber, password)     -> PassengerDto
 *   signUpDriver(email, name, phoneNumber, password)        -> DriverDto
 */

const AuthUI = {
    role: 'PASSENGER',   // 'PASSENGER' | 'DRIVER'
    mode: 'signin',      // 'signin' | 'signup'
    isSubmitting: false,

    setRole(role) {
        this.role = role;
        document.getElementById('role-passenger-btn').classList.toggle('active', role === 'PASSENGER');
        document.getElementById('role-driver-btn').classList.toggle('active', role === 'DRIVER');
        this._refreshLabels();
    },

    setMode(mode) {
        this.mode = mode;
        document.getElementById('tab-signin').classList.toggle('active', mode === 'signin');
        document.getElementById('tab-signup').classList.toggle('active', mode === 'signup');

        const showSignupFields = mode === 'signup';
        document.getElementById('field-name').style.display = showSignupFields ? 'block' : 'none';
        document.getElementById('field-phone').style.display = showSignupFields ? 'block' : 'none';
        document.getElementById('input-name').required = showSignupFields;
        document.getElementById('input-phone').required = showSignupFields;

        document.getElementById('auth-footnote').innerHTML = mode === 'signup'
            ? 'Already have an account? <a href="#" onclick="AuthUI.setMode(\'signin\'); return false;" style="color:var(--auth-accent); text-decoration:none;">Sign in</a>'
            : 'New here? <a href="#" onclick="AuthUI.setMode(\'signup\'); return false;" style="color:var(--auth-accent); text-decoration:none;">Create an account</a>';

        this._hideError();
        this._refreshLabels();
    },

    _refreshLabels() {
        const roleLabel = this.role === 'DRIVER' ? 'driver' : 'passenger';
        document.getElementById('auth-subtitle').textContent = this.mode === 'signup'
            ? `Create your ${roleLabel} account`
            : 'Sign in to continue';
        document.getElementById('auth-submit-btn').textContent = this.mode === 'signup'
            ? `Create ${roleLabel} account`
            : `Sign in as ${roleLabel}`;
    },

    async handleSubmit(event) {
        event.preventDefault();
        if (this.isSubmitting) return false;

        this._hideError();

        const email = document.getElementById('input-email').value.trim();
        const password = document.getElementById('input-password').value;

        if (!email || !password) {
            this._showError('Email and password are required.');
            return false;
        }

        try {
            this._setLoading(true);

            if (this.mode === 'signin') {
                await this._doSignIn(email, password);
            } else {
                await this._doSignUp(email, password);
            }
        } catch (err) {
            this._showError(this._friendlyError(err));
        } finally {
            this._setLoading(false);
        }

        return false;
    },

    async _doSignIn(email, password) {
        const res = await ApiClient.signIn(email, password);
        console.log('Full res:', JSON.stringify(res));        // ← add this
        console.log('res.data:', JSON.stringify(res.data));

        // res.data = { id, success, role } from AuthResponseDto
        if (!res.data || !res.data.role || !res.data.id) {
            throw new Error('Server did not return account details.');
        }

        const role = res.data.role;
        const id = res.data.id;

        this._redirectToApp(role, id);
    },

    async _doSignUp(email, password) {
        const name = document.getElementById('input-name').value.trim();
        const phoneNumber = document.getElementById('input-phone').value.trim();

        if (!name || !phoneNumber) {
            throw new Error('Name and phone number are required to create an account.');
        }

        const res = this.role === 'DRIVER'
            ? await ApiClient.signUpDriver(email, name, phoneNumber, password)
            : await ApiClient.signUpPassenger(email, name, phoneNumber, password);

        if (!res.success) {
            throw new Error('Account creation failed.');
        }

        // Account created — switch to sign-in mode so they log in with the same credentials
        this.setMode('signin');
        document.getElementById('input-password').value = '';
        this._showError('Account created. Sign in below to continue.', true);
    },

    _redirectToApp(role, id) {
        const idParam = role === 'DRIVER' ? 'driverId' : 'passengerId';
        const roleParam = role === 'DRIVER' ? 'driver' : 'passenger';
        window.location.href = `index.html?role=${roleParam}&${idParam}=${id}`;
    },

    _setLoading(isLoading) {
        this.isSubmitting = isLoading;
        const btn = document.getElementById('auth-submit-btn');
        btn.disabled = isLoading;
        if (isLoading) {
            btn.dataset.label = btn.textContent;
            btn.innerHTML = `<span class="spinner"></span>Working...`;
        } else if (btn.dataset.label) {
            btn.textContent = btn.dataset.label;
        }
    },

    _showError(message, isInfo = false) {
        const el = document.getElementById('auth-error');
        el.textContent = message;
        el.style.display = 'block';
        el.style.color = isInfo ? 'var(--auth-accent)' : 'var(--auth-red)';
        el.style.background = isInfo ? 'rgba(62,207,142,0.1)' : 'rgba(229,72,77,0.1)';
        el.style.borderColor = isInfo ? 'rgba(62,207,142,0.35)' : 'rgba(229,72,77,0.35)';
    },

    _hideError() {
        const el = document.getElementById('auth-error');
        el.style.display = 'none';
    },

    _friendlyError(err) {
        if (err?.status === 401 || err?.status === 403) {
            return 'Invalid email or password.';
        }
        if (err?.status === 409) {
            return 'An account with this email already exists.';
        }
        return err?.message || 'Something went wrong. Please try again.';
    }
};

// Initialize labels on load
document.addEventListener('DOMContentLoaded', () => {
    AuthUI.setRole('PASSENGER');
    AuthUI.setMode('signin');
});

if (typeof module !== 'undefined' && module.exports) module.exports = AuthUI;
