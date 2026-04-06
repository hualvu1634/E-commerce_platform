import axiosClient from "./axiosClient";

const orderApi = {
    checkout(data) {
        return axiosClient.post('/orders/checkout', data);
    },
    getOrders(page = 1, pageSize = 10) {
        return axiosClient.get(`/orders?page=${page}&pageSize=${pageSize}`);
    },
    checkStatus(orderId) {
        return axiosClient.get(`/orders/${orderId}/status`);
    },
    simulatePayment(paymentData) {
        return axiosClient.post('/payments/sepay-webhook', paymentData);
    }
};
export default orderApi;