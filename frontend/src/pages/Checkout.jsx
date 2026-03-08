import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate, Link } from 'react-router-dom';
import orderApi from '../api/orderApi';

const Checkout = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { items, totalMoney } = location.state || { items: [], totalMoney: 0 };
  
  const [customerInfo, setCustomerInfo] = useState({ customerName: '', phoneNumber: '', address: '' });
  const [isPaid, setIsPaid] = useState(false);
  const [loading, setLoading] = useState(false); // Thêm state để chặn bấm nhiều lần

  useEffect(() => {
    if (!items || items.length === 0) navigate('/cart');
  }, [items, navigate]);

  const handleInputChange = (e) => setCustomerInfo({ ...customerInfo, [e.target.name]: e.target.value });

  const handleSubmitOrder = async (e) => {
    e.preventDefault();
    setLoading(true);

    const orderData = {
      customerName: customerInfo.customerName,
      phoneNumber: customerInfo.phoneNumber,
      address: customerInfo.address,
      itemRequests: items.map(item => ({ 
        productId: item.productId || item.id, 
        quantity: item.quantity 
      }))
    };

    try {
      // 1. Gửi đơn hàng lên server
      await orderApi.checkout(orderData); 
      
      // 2. Vì bạn muốn bỏ qua bước quét QR, ta xác nhận thành công luôn tại giao diện
      setIsPaid(true); 
    } catch (err) { 
      alert("Lỗi đặt hàng! Vui lòng thử lại."); 
    } finally {
      setLoading(false);
    }
  };

  const shippingFee = 30000;
  const finalAmount = totalMoney + shippingFee;

  // GIAO DIỆN KHI THÀNH CÔNG
  if (isPaid) {
    return (
      <div className="container mt-5 text-center">
        <div className="card shadow border-0 p-5 mx-auto" style={{ maxWidth: '500px' }}>
        
          <h3 className="fw-bold">Đặt hàng thành công!</h3>
          <p className="text-muted">Đơn hàng của bạn đã được tiếp nhận và đang trong quá trình xử lý.</p>
          <button className="btn btn-danger rounded-0 mt-3 px-4" onClick={() => navigate('/')}>
            TIẾP TỤC MUA SẮM
          </button>
             <button className="btn btn-danger rounded-0 mt-3 px-4" onClick={() => navigate('/orders')}>
            ĐƠN HÀNG ĐÃ MUA
          </button>
        </div>
      </div>
    );
  }

  // GIAO DIỆN NHẬP THÔNG TIN (FORM)
  return (
    <div className="container mt-4 mb-5">
      <nav aria-label="breadcrumb">
        <ol className="breadcrumb mb-5">
          <li className="breadcrumb-item">
            <Link to="/" className="text-danger text-decoration-none fw-bold">Trang chủ</Link>
          </li>
          <li className="breadcrumb-item active">Thanh toán</li>
        </ol>
      </nav>
      <form onSubmit={handleSubmitOrder}>
        <div className="row g-4">
          <div className="col-md-7">
            <div className="p-4 bg-white border shadow-sm">
              <h5 className="mb-4 fw-bold text-uppercase border-bottom pb-2">Thông tin giao hàng</h5>
              <div className="mb-4">
                <label className="form-label small fw-bold">Người nhận hàng *</label>
                <input type="text" name="customerName" className="form-control rounded-0" required onChange={handleInputChange} />
              </div>
              <div className="mb-4">
                <label className="form-label small fw-bold">Số điện thoại *</label>
                <input type="text" name="phoneNumber" className="form-control rounded-0" required onChange={handleInputChange} />
              </div>
              <div className="mb-2">
                <label className="form-label small fw-bold">Địa chỉ nhận hàng *</label>
                <textarea name="address" className="form-control rounded-0" rows="3" required onChange={handleInputChange}></textarea>
              </div>
            </div>
          </div>
          <div className="col-md-5">
            <div className="p-4 bg-white border shadow-sm h-100">
              <h5 className="mb-4 fw-bold text-uppercase border-bottom pb-2">Đơn hàng của bạn</h5>
              <div className="mb-4">
                {items.map((item, idx) => (
                  <div key={idx} className="d-flex justify-content-between small mb-2">
                    <span className="text-truncate" style={{maxWidth: '70%'}}>{item.productName || item.name} x{item.quantity}</span>
                    <span className="fw-bold">{(item.price * item.quantity).toLocaleString()}đ</span>
                  </div>
                ))}
              </div>
              <hr />
              <div className="d-flex justify-content-between mb-2 text-muted"><span>Tạm tính:</span><span>{totalMoney?.toLocaleString()}đ</span></div>
              <div className="d-flex justify-content-between mb-2 text-muted"><span>Phí vận chuyển:</span><span>{shippingFee.toLocaleString()}đ</span></div>
              <div className="d-flex justify-content-between mt-4 mb-4">
                <h5 className="fw-bold">Tổng cộng:</h5>
                <h5 className="fw-bold text-danger">{finalAmount.toLocaleString()}đ</h5>
              </div>
              <button 
                type="submit" 
                className="btn btn-danger w-100 rounded-0 py-3 fw-bold text-uppercase"
                disabled={loading}
              >
                {loading ? 'ĐANG XỬ LÝ...' : 'XÁC NHẬN ĐẶT HÀNG'}
              </button>
            </div>
          </div>
        </div>
      </form>
    </div>
  );
};

export default Checkout;