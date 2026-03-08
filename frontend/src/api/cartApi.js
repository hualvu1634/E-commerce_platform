import axiosClient from "./axiosClient";

const cartApi = {
    getCart() {
        return axiosClient.get('/carts');
    },
    addToCart(data) {
        return axiosClient.post('/carts', data);
    },
    updateCart(data) {
        return axiosClient.put('/carts', data); 
    },
    // Thêm hàm xóa sản phẩm khỏi giỏ
    removeFromCart(productId) {
        return axiosClient.delete(`/carts/item/${productId}`);
    }
};
export default cartApi;