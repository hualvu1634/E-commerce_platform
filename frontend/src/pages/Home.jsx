import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import productApi from '../api/productApi'; // Import Product API
import cartApi from '../api/cartApi';       // Import Cart API

const Home = () => {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [sortOrder, setSortOrder] = useState('default'); 
  const navigate = useNavigate();

  // Load danh mục
  useEffect(() => {
    const fetchCategories = async () => {
        try {
            const res = await productApi.getCategories();
            setCategories(res.data);
        } catch (error) { console.error(error); }
    };
    fetchCategories();
  }, []);

  // Load sản phẩm
  useEffect(() => {
    const fetchProducts = async () => {
        try {
            let res;
            if (selectedCategory) {
                res = await productApi.getByCategory(selectedCategory, { page: 1 });
            } else {
                res = await productApi.getAll({ page: 1 });
            }
            
            let data = res.data.data || [];
            // Xử lý sắp xếp (Client side)
            if (sortOrder === 'asc') data.sort((a, b) => a.price - b.price);
            else if (sortOrder === 'desc') data.sort((a, b) => b.price - a.price);
            
            setProducts(data);
        } catch (error) { setProducts([]); }
    };
    fetchProducts();
  }, [selectedCategory, sortOrder]);

  const handleAddToCart = async (productId) => {
    const token = localStorage.getItem('token');
    if (!token) { navigate('/login'); return; }
    try {
      await cartApi.addToCart({ productId, quantity: 1 }); // Gọi API Cart
    
    } catch (err) { alert("Lỗi khi thêm vào giỏ!"); }
  };

  const handleBuyNow = (product) => {
    const token = localStorage.getItem('token');
    if (!token) { navigate('/login'); return; }
    
    navigate('/checkout', {
      state: {
        items: [{ productId: product.id, productName: product.name, price: product.price, quantity: 1 }],
        totalMoney: product.price,
        orderType: 'BUY_NOW'
      }
    });
  };

  // ... Phần return giao diện giữ nguyên hoàn toàn ...
  return (
    <div className="container mt-4 mb-5">
       <nav aria-label="breadcrumb">
        <ol className="breadcrumb">
          <li className="breadcrumb-item"><Link to="/" className="text-danger text-decoration-none fw-bold">Trang chủ</Link></li>
          <li className="breadcrumb-item active">Sản phẩm</li>
        </ol>
      </nav>

      <div className="row mt-4">
        {/* --- CỘT TRÁI (SIDEBAR) --- */}
        <aside className="col-md-3">
          <div className="mb-4 bg-white p-3 border rounded-1 shadow-sm">
            <h6 className="fw-bold border-bottom border-danger pb-2 d-inline-block border-2 mb-3 text-uppercase">Danh mục</h6>
            <ul className="list-unstyled mb-0">
              <li className="mb-2">
                <button className={`btn btn-link text-decoration-none p-0 w-100 text-start ${!selectedCategory ? 'text-danger fw-bold' : 'text-dark'}`} onClick={() => setSelectedCategory(null)}>Tất cả sản phẩm</button>
              </li>
              {categories.map(cat => (
                <li key={cat.id} className="mb-2">
                  <button className={`btn btn-link text-decoration-none p-0 w-100 text-start ${selectedCategory === cat.id ? 'text-danger fw-bold' : 'text-dark'}`} onClick={() => setSelectedCategory(cat.id)}>{cat.name}</button>
                </li>
              ))}
            </ul>
          </div>
          {/* ... Phần sắp xếp ... */}
           <div className="mb-4 bg-white p-3 border rounded-1 shadow-sm">
            <h6 className="fw-bold border-bottom border-danger pb-2 d-inline-block border-2 mb-3 text-uppercase">Sắp xếp theo giá</h6>
             {/* ... (Copy lại các input radio sort từ code cũ) ... */}
              <div className="form-check mb-2"><input className="form-check-input shadow-none" type="radio" name="sortPrice" id="sortDefault" checked={sortOrder === 'default'} onChange={() => setSortOrder('default')} /><label className="form-check-label small" htmlFor="sortDefault">Mặc định</label></div>
              <div className="form-check mb-2"><input className="form-check-input shadow-none" type="radio" name="sortPrice" id="sortAsc" onChange={() => setSortOrder('asc')} /><label className="form-check-label small" htmlFor="sortAsc">Giá tăng dần</label></div>
              <div className="form-check mb-2"><input className="form-check-input shadow-none" type="radio" name="sortPrice" id="sortDesc" onChange={() => setSortOrder('desc')} /><label className="form-check-label small" htmlFor="sortDesc">Giá giảm dần</label></div>
          </div>
        </aside>

        {/* --- CỘT PHẢI --- */}
        <main className="col-md-9">
          <div className="row g-3">
            {products.length > 0 ? products.map(p => (
              <div className="col-md-4 col-sm-6" key={p.id}>
                <div className="card h-100 rounded-0 border shadow-sm position-relative">
              
                   <div className="position-absolute top-0 start-0 m-2 bg-danger text-white px-2 py-1 text-uppercase fw-bold" style={{fontSize: '10px', zIndex: 1}}>HOT</div>
                {( p.imageUrl) ? (
  <img
    src={ p.imageUrl}
    alt={p.name}
    className="card-img-top bg-white p-3"
    style={{ height: '200px', objectFit: 'contain' }}
  />
) : (
  <div
    className="bg-light d-flex align-items-center justify-content-center p-3"
    style={{ height: '200px' }}
  >
    <span className="text-muted small">Không có ảnh</span>
  </div>
)}
                  <div className="card-body pb-1 d-flex flex-column">
                    <h6 className="card-title fw-bold mb-1 text-truncate" title={p.name}>{p.name}</h6>
                    <p className="small text-muted mb-2 text-truncate-2" style={{ height: '40px', overflow: 'hidden' }}>{p.description || 'Đang cập nhật mô tả...'}</p>
                    <div className="text-warning mb-2" style={{fontSize: '11px'}}>★★★★★</div>
                    <div className="mt-auto"><h5 className="text-danger fw-bold mb-2">{p.price?.toLocaleString()}đ</h5></div>
                  </div>
                  <div className="card-footer bg-white border-0 d-flex gap-2 pb-3 pt-0">
                    <button className="btn btn-danger flex-grow-1 rounded-0 text-uppercase fw-bold" style={{fontSize: '12px'}} onClick={() => handleBuyNow(p)}>Mua ngay</button>
                    <button className="btn btn-outline-danger rounded-0 px-3" onClick={() => handleAddToCart(p.id)}> <i className="fa-solid fa-cart-shopping" ></i></button>
                  </div>
                </div>
              </div>
            )) : <p className="text-center mt-5 text-muted w-100">Không tìm thấy sản phẩm nào.</p>}
          </div>
        </main>
      </div>
    </div>
  );
};
export default Home;    