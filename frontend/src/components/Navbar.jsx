import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import userApi from '../api/userApi'; // Đảm bảo bạn đã có file này nhé

const Navbar = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const token = localStorage.getItem('token');
  
  // Thêm state để quản lý việc hiển thị menu và tên người dùng
  const [showDropdown, setShowDropdown] = useState(false);
  const [userName, setUserName] = useState(localStorage.getItem('username') || 'Người dùng');

  // Gọi API lấy tên người dùng khi có token
  useEffect(() => {
    if (token) {
      const fetchUser = async () => {
        try {
          const res = await userApi.getProfile();
          if (res.data && res.data.name) {
            setUserName(res.data.name);
          }
        } catch (error) {
          console.error("Lỗi lấy thông tin user:", error);
        }
      };
      fetchUser();
    }
  }, [token]);



  const handleLogout = () => {
    if (window.confirm("Bạn có chắc chắn muốn đăng xuất?")) {
      localStorage.removeItem('token');
      localStorage.removeItem('username');
      setShowDropdown(false);
      navigate('/login');
    }
  };

  return (
    <nav className="navbar navbar-expand-lg py-2 shadow sticky-top" style={{ backgroundColor: '#d70018' }}>
      <div className="container d-flex align-items-center justify-content-between">
        
        {/* Logo */}
        <Link className="navbar-brand text-white text-lowercase" to="/" style={{ fontFamily: "'Brush Script MT', cursive", fontWeight: 'bold', fontSize: '2.2rem' }}>
          hualvu
        </Link>
        
        {/* Thanh Tìm Kiếm */}
        <form className="d-flex mx-auto" style={{ width: '45%' }}>
          <div className="input-group">
            <span className="input-group-text bg-white border-0 rounded-start-pill ps-3 pe-2">
              <i className="fa-solid fa-magnifying-glass" style={{ color: '#6c757d' }}></i>
            </span>
            <input 
              type="text" 
              className="form-control border-0 rounded-end-pill py-2 shadow-none" 
              placeholder="Bạn muốn mua gì hôm nay?" 
            />
          </div>
        </form>

        <div className="d-flex align-items-center gap-3 ms-auto">
          {/* Nút Giỏ Hàng */}
          <Link to="/cart" className="btn text-white d-flex align-items-center gap-2 rounded px-3" style={{ backgroundColor: 'rgba(255,255,255,0.2)' }}>
            <div style={{ fontSize: '0.85rem' }}>Giỏ hàng</div>
          </Link>
          
          {/* Khu vực Người Dùng & Dropdown */}
          {token ? (
            <div 
              className="position-relative"
              onMouseEnter={() => setShowDropdown(true)}
              onMouseLeave={() => setShowDropdown(false)}
              style={{ padding: '10px 0' }} // Padding để vùng hover rộng hơn, chuột không bị trượt
            >
              <div 
                className="btn text-white d-flex align-items-center gap-2 rounded px-3" 
                style={{ backgroundColor: 'rgba(255,255,255,0.2)', cursor: 'pointer' }}
              >
                <i className="fa-regular fa-user"></i>
                <div style={{ fontSize: '0.85rem' }}>{userName}</div>
              </div>

              {/* Menu Dropdown */}
              {showDropdown && (
                <div 
                  className="dropdown-menu dropdown-menu-end shadow-sm border-0 p-0" 
                  style={{ 
                    position: 'absolute', 
                    top: '100%', 
                    right: 0, 
                    minWidth: '200px',
                    display: 'block', 
                    borderRadius: '4px',
                    marginTop: '-2px'
                  }}
                >
                  {/* Mũi tên nhọn chỉ lên trên giống ảnh thiết kế */}
                  <div style={{
                    position: 'absolute',
                    top: '-8px',
                    right: '30px',
                    width: '0',
                    height: '0',
                    borderLeft: '8px solid transparent',
                    borderRight: '8px solid transparent',
                    borderBottom: '8px solid white'
                  }}></div>

                  <div className="py-2">
                   
                    <Link 
                      className="dropdown-item py-2 px-3 text-dark" 
                      to="/orders" 
                      onClick={() => setShowDropdown(false)}
                      style={{ fontSize: '15px' }}
                    >
                      Đơn Mua
                    </Link>
                    <button 
                      className="dropdown-item py-2 px-3 text-dark" 
                      onClick={handleLogout}
                      style={{ fontSize: '15px' }}
                    >
                      Đăng Xuất
                    </button>
                  </div>
                </div>
              )}
            </div>
          ) : (
            <Link to="/login" className="btn text-white d-flex align-items-center gap-2 rounded px-3" style={{ backgroundColor: 'rgba(255,255,255,0.2)' }}>
              <div style={{ fontSize: '0.85rem' }}>Đăng nhập</div>
            </Link>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;