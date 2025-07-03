import secureApi from '../secureApi';

export async function isAuthenticated() {
    try {
        const res = await secureApi.get('/v1/user/session');
        return res.status === 200;
    } catch (error) {
        return false;
    }
}
