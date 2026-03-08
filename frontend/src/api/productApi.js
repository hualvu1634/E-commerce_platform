import axiosClient from "./axiosClient";

const productApi = {
    getAll(params) {
        return axiosClient.get('/products', { params });
    },
    getByCategory(categoryId) {
        return axiosClient.get(`/categories/${categoryId}/products`);
    },
    getCategories() {
        return axiosClient.get('/categories');
    }
};

export default productApi;