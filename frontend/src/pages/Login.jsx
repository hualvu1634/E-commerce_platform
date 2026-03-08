import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import authApi from '../api/authApi'; // Import API



const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      // Gọi API qua hàm đã định nghĩa
      const res = await authApi.login({ username, password });
      
      const token = res.data.token || res.data.accessToken; 
      if(token) {
        localStorage.setItem('token', token);
        localStorage.setItem('username', res.data.username || username); 
        navigate('/'); 
      }
    } catch (err) {
      alert("Sai tài khoản hoặc mật khẩu!");
    }
  };

  // ... Phần return giao diện giữ nguyên như cũ ...
  return (
    <div className="container mt-5 mb-5">
      <div className="mx-auto bg-white p-4 shadow-sm" style={{maxWidth: '450px'}}>
        <h3 className="mb-4 fw-bold">Đăng nhập</h3>
        <form onSubmit={handleLogin}>
          <div className="mb-3">
            <label className="form-label text-muted small">Tài khoản</label>
            <input type="text" className="form-control rounded-0" onChange={(e) => setUsername(e.target.value)} required />
          </div>
          <div className="mb-3">
            <label className="form-label text-muted small">Mật khẩu</label>
            <input type="password" className="form-control rounded-0" onChange={(e) => setPassword(e.target.value)} required />
          </div>
          {/* ... giữ nguyên phần checkbox và link ... */}
          <button type="submit" className="btn btn-danger w-100 rounded-0 py-2 fw-bold mb-4">ĐĂNG NHẬP</button>
        </form>
        <div className="text-center mt-2">
          <span className="text-muted">Chưa có tài khoản? </span>
          <Link to="/signup" className="text-danger text-decoration-none fw-bold">Đăng ký ngay</Link>
        </div>
      </div>
    </div>
  );
};
export default Login;