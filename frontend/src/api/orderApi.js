import axiosClient from "./axiosClient";

const orderApi = {
    checkout(data) {
        return axiosClient.post('/orders/checkout', data);
    },
    getOrders(page = 1, pageSize = 10) {
        return axiosClient.get(`/orders?page=${page}&pageSize=${pageSize}`);
    }

};
export default orderApi;