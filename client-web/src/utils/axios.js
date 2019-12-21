import axios from 'axios';

const baseUrl = "";

const axiosInstance = axios.create({
    timeout: 16 * 1000,
    headers: {
        'Content-Type': 'application/json;charset=utf-8',
        'Accept': 'application/json'
    }
});

axiosInstance.interceptors.request.use(requestConfig => {
    requestConfig.baseURL = baseUrl;
    return requestConfig;
});

export default axiosInstance;