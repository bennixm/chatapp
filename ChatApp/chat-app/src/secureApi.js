import axios from 'axios';

const secureApi = axios.create({
    baseURL: 'http://localhost:8085/api',
    withCredentials: true,
});


secureApi.interceptors.response.use(
    response => response,
    error => {
        console.error('API error:', error);
        return Promise.reject(error);
    }
);

export default secureApi;
