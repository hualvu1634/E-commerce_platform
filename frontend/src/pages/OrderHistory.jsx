import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import orderApi from '../api/orderApi';

const OrderHistory = () => {
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const res = await orderApi.getOrders(1, 10);
        let data = res.data.data || res.data.content || [];

        const sortedData = data.sort((a, b) => 
            new Date(b.orderDate) - new Date(a.orderDate)
        );

        setOrders(sortedData);
      } catch (error) { 
        console.error("Lỗi tải đơn mua:", error); 
      }
    };
    fetchOrders();
  }, []);

  const formatStatus = (status) => {
    switch(status) {
        case 'PENDING': return <span className="badge bg-warning text-dark">Chờ xác nhận</span>;
        case 'SUCCESS': return <span className="badge bg-success">Đã giao</span>;
        case 'CANCELLED': return <span className="badge bg-danger">Đã hủy</span>;
        default: return <span className="badge bg-secondary">{status}</span>;
    }
  }

  return (
    <div className="container mt-4 mb-5">
      <nav aria-label="breadcrumb">
        <ol className="breadcrumb">
          <li className="breadcrumb-item">
            <Link to="/" className="text-danger text-decoration-none fw-bold">Trang chủ</Link>
          </li>
          <li className="breadcrumb-item active">Lịch sử đơn hàng</li>
        </ol>
      </nav>

      <div className="card shadow-sm border-0 mt-4">
        <div className="card-header bg-white py-3">
          <h5 className="mb-0 fw-bold">Đơn mua của tôi</h5>
        </div>
        <div className="card-body p-0">
          <div className="table-responsive">
            <table className="table table-hover align-middle mb-0">
              <thead className="table-light">
                <tr>
                  <th className="ps-4" style={{ width: '35%' }}>Tên sản phẩm</th>
                  <th className="text-center">Số lượng</th>
                  <th className="text-center">Đơn giá</th>
                  <th className="text-center">Thanh toán</th>
                  <th className="text-center">Trạng thái</th>
                </tr>
              </thead>
              <tbody>
                {orders.length > 0 ? orders.map((order) => (
                  <React.Fragment key={order.orderId}>
                    {/* Hàng hiển thị mã đơn và ngày đặt */}
                    <tr className="table-secondary py-1" style={{ fontSize: '0.85rem' }}>
                      <td colSpan="5" className="ps-4">
                        <span className="fw-bold text-dark">Mã đơn: #{order.orderId}</span>
                        <span className="ms-3 text-muted">Ngày: {new Date(order.orderDate).toLocaleDateString('vi-VN')}</span>
                      </td>
                    </tr>
                    
                    {/* Danh sách các item trong đơn hàng */}
                    {order.items?.map((item, idx) => (
                      <tr key={idx}>
                        <td className="ps-4">
                          <div className="fw-bold text-truncate" style={{ maxWidth: '300px' }}>
                            {item.productName}
                          </div>
                        </td>
                        <td className="text-center">x{item.quantity}</td>
                        <td className="text-center text-muted">
                          {item.price?.toLocaleString()}đ
                        </td>
                        {/* Cột Thanh toán và Trạng thái gộp (chỉ hiện ở dòng đầu của mỗi đơn) */}
                        {idx === 0 ? (
                          <>
                            <td className="text-center fw-bold text-danger" rowSpan={order.items.length}>
                              {order.totalMoney?.toLocaleString()}đ
                            </td>
                            <td className="text-center" rowSpan={order.items.length}>
                              {formatStatus(order.status)}
                            </td>
                          </>
                        ) : null}
                      </tr>
                    ))}
                  </React.Fragment>
                )) : (
                  <tr>
                    <td colSpan="5" className="text-center py-5">
                      <div className="mb-3" style={{ fontSize: '3rem' }}>🧾</div>
                      <p className="text-muted">Bạn chưa có đơn hàng nào</p>
                      <Link to="/" className="btn btn-danger btn-sm px-4">Mua sắm ngay</Link>
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};

export default OrderHistory;