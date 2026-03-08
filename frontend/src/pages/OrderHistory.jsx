import React, { useState, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import orderApi from '../api/orderApi';

const OrderHistory = () => {
  const [orders, setOrders] = useState([]);
  const location = useLocation();

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        // Lấy trang 1, 10 đơn hàng. Bạn có thể thêm phân trang sau nếu muốn.
        const res = await orderApi.getOrders(1, 10);
        // Dựa vào PageResponse (data hoặc content tùy cách backend config)
        setOrders(res.data.data || res.data.content || []); 
      } catch (error) { console.error("Lỗi tải đơn mua:", error); }
    };
    fetchOrders();
  }, []);

  // Hàm chuyển đổi OrderStatus sang tiếng Việt (Tuỳ chỉnh theo OrderStatus enum ở backend)
  const formatStatus = (status) => {
    switch(status) {
        case 'PENDING': return <span className="text-warning fw-bold">Chờ xác nhận</span>;
        case 'SUCCESS': return <span className="text-success fw-bold">Đã giao</span>;
        case 'CANCELLED': return <span className="text-danger fw-bold">Đã hủy</span>;
        default: return <span className="text-muted fw-bold">{status}</span>;
    }
  }

  return (
    <div className="container mt-4 mb-5">
      <nav aria-label="breadcrumb">
        <ol className="breadcrumb">
          <li className="breadcrumb-item"><Link to="/" className="text-danger text-decoration-none fw-bold">Trang chủ</Link></li>
          <li className="breadcrumb-item active">Đơn mua của tôi</li>
        </ol>
      </nav>

      <div className="row mt-4">
    

        <main className="col-md-9">
          {orders.length > 0 ? orders.map(order => (
            <div key={order.orderId} className="bg-white p-4 border rounded-1 shadow-sm mb-3">
              <div className="d-flex justify-content-between border-bottom pb-2 mb-3">
                  <div className="text-muted">
                    <span className="fw-bold me-2 text-dark">Mã đơn hàng: #{order.orderId}</span> 
                    Ngày đặt: {new Date(order.orderDate).toLocaleDateString('vi-VN')}
                  </div>
                  <div className="text-uppercase" style={{fontSize: '14px'}}>
                      {formatStatus(order.status)}
                  </div>
              </div>
              
              {/* Danh sách sản phẩm trong đơn */}
              {order.items?.map((item, idx) => (
                <div key={idx} className="d-flex mb-3 border-bottom pb-3">
                  
                  <div className="flex-grow-1">
                      <h6 className="fw-bold mb-1">{item.productName}</h6>
                      <div className="text-muted small">x{item.quantity}</div>
                  </div>
                  <div className="text-danger fw-bold d-flex align-items-center">
                      {item.price?.toLocaleString()}đ
                  </div>
                </div>
              ))}

              <div className="d-flex justify-content-end align-items-center pt-2">
                  <div className="me-3 text-muted">Thành tiền:</div>
                  <h4 className="text-danger fw-bold mb-0">{order.totalMoney?.toLocaleString()}đ</h4>
              </div>
            </div>
          )) : (
            <div className="bg-white p-5 border rounded-1 shadow-sm text-center">
               <div className="mb-3"><span style={{ fontSize: '4rem', color: '#ccc' }}>🧾</span></div>
               <h5 className="fw-bold text-muted">Bạn chưa có đơn hàng nào</h5>
               <Link to="/" className="btn btn-danger mt-3 rounded-0 px-4">Tiếp tục mua sắm</Link>
            </div>
          )}
        </main>
      </div>
    </div>
  );
};

export default OrderHistory;