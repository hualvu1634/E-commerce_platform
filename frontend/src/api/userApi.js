import axiosClient from "./axiosClient";

const userApi = {
    getProfile() {
        return axiosClient.get('/users/profile');
    }
};

export default userApi;