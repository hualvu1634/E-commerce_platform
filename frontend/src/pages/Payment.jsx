import React, { useEffect, useState, useRef } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import orderApi from '../api/orderApi';

const Payment = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { orderId } = location.state || {};
  
  const [isPaid, setIsPaid] = useState(false);
  const [countdown, setCountdown] = useState(5);
  const intervalRef = useRef(null);

  useEffect(() => {
    if (!orderId) navigate('/');
  }, [orderId, navigate]);

  useEffect(() => {
    if (!orderId || isPaid) return;

    const checkPaymentStatus = async () => {
      try {
        const response = await orderApi.checkStatus(orderId);

        if (response.data.status === 'SUCCESS' || response.data === 'SUCCESS') {
          setIsPaid(true);
        }
      } catch (error) {
        console.error("Lỗi check status:", error);
      }
    };

    checkPaymentStatus();
    intervalRef.current = setInterval(checkPaymentStatus, 3000);

    return () => clearInterval(intervalRef.current);
  }, [orderId, isPaid]);

  useEffect(() => {
    if (isPaid) {
      clearInterval(intervalRef.current);
      const timer = setInterval(() => {
        setCountdown((prev) => {
          if (prev <= 1) {
            clearInterval(timer);
            navigate('/');
            return 0;
          }
          return prev - 1;
        });
      }, 1000);
      return () => clearInterval(timer);
    }
  }, [isPaid, navigate]);

  if (!orderId) return null;

  if (isPaid) {
    return (
      <div className="container mt-5 text-center">
        <div className="card shadow border-0 p-5 mx-auto" style={{ maxWidth: '500px' }}>
          <i className="fa-solid fa-circle-check text-success mb-3" style={{ fontSize: '60px' }}></i>
          <h3 className="fw-bold text-success">Thanh toán thành công!</h3>
          <p className="text-danger fw-bold mt-3">Tự động quay về trang chủ sau {countdown} giây...</p>
        </div>
      </div>
    );
  }

  const qrUrl = `https://qr.sepay.vn/img?bank=BIDV&acc=8820382907&template=compact&amount=2000&des=DH${orderId}`;

  return (
    <div className="container mt-5 text-center">
      <div className="card shadow border-0 p-4 mx-auto" style={{ maxWidth: '500px' }}>
        <h4 className="fw-bold text-primary mb-4">Quét mã để thanh toán</h4>
        <p className="text-muted mb-4">
          Mã đơn hàng: <span className="fw-bold text-dark">DH{orderId}</span><br/>
          <span className="small text-danger">Vui lòng không thay đổi nội dung chuyển khoản</span>
        </p>
        
        <div className="bg-light p-3 rounded mb-4 mx-auto" style={{ width: 'fit-content' }}>
          <img src={qrUrl} alt="QR Code" className="img-fluid rounded" style={{ minWidth: '250px', minHeight: '250px' }} />
          <div className="mt-3 d-flex align-items-center justify-content-center">
            <div className="spinner-border text-primary spinner-border-sm" role="status"></div>
            <span className="ms-2 small text-muted"> Hệ thống đang chờ thanh toán...</span>
          </div>
        </div>

        <button className="btn btn-danger w-100 mt-2" onClick={() => navigate('/orders')}>
          Xem đơn hàng của tôi
        </button>
      </div>
    </div>
  );
};

export default Payment;