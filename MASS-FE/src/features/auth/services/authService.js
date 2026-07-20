import axiosClient from '../../../shared/services/axiosClient';

const authService = {
    login: async (payload) => {
        console.log("Login payload:", payload);
        const response = await axiosClient.post('/api/v1/auths/login', payload);
        return response.data;
    },
    register: async (payload) => {
        console.log("Register payload:", payload);
        const response = await axiosClient.post('/api/v1/auths/register', payload);
        return response.data;
    },
    logout: async () => {
        console.log("Logout called");
        const response = await axiosClient.post('/api/v1/auths/logout');
        return response.data;
    }
}

export default authService;