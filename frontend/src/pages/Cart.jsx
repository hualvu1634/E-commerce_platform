import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import cartApi from '../api/cartApi';

const Cart = () => {
  const [cartItems, setCartItems] = useState([]);
  const [selectedItems, setSelectedItems] = useState([]);
  const navigate = useNavigate();

  const fetchCart = async () => {
    try {
        const res = await cartApi.getCart();
        setCartItems(res.data);
    } catch (err) { console.error(err); }
  };

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) { navigate('/login'); return; }
    fetchCart();
  }, [navigate]);

  const handleSelectOne = (productId) => {
    if (selectedItems.includes(productId)) setSelectedItems(selectedItems.filter(id => id !== productId));
    else setSelectedItems([...selectedItems, productId]);
  };

  const handleSelectAll = (e) => {
    if (e.target.checked) setSelectedItems(cartItems.map(item => item.productId));
    else setSelectedItems([]);
  };

  const calculateTotal = () => {
    return cartItems.reduce((total, item) => {
      if (selectedItems.includes(item.productId)) return total + (item.price * item.quantity);
      return total;
    }, 0);
  };

  const handleCheckout = () => {
    const itemsToCheckout = cartItems.filter(item => selectedItems.includes(item.productId));
    if (itemsToCheckout.length === 0) { alert("Vui lòng chọn ít nhất một sản phẩm để thanh toán!"); return; }
    navigate('/checkout', { state: { items: itemsToCheckout, totalMoney: calculateTotal(), orderType: 'CART' } });
  };

  // --- THÊM MỚI: Xử lý thay đổi số lượng ---
  const handleQuantityChange = async (productId, currentQuantity, change) => {
    const newQuantity = currentQuantity + change;
    if (newQuantity < 1) return; // Không cho phép giảm dưới 1

    // Cập nhật giao diện tạm thời (Optimistic Update) để trải nghiệm mượt hơn
    const updatedItems = cartItems.map(item =>
      item.productId === productId ? { ...item, quantity: newQuantity } : item
    );
    setCartItems(updatedItems);

    try {
      // Gọi API cập nhật (Backend yêu cầu truyền mảng/list ItemRequest)
      await cartApi.updateCart([{ productId: productId, quantity: newQuantity }]);
    } catch (err) {
      console.error("Lỗi cập nhật số lượng:", err);
      // Nếu lỗi, rollback lại dữ liệu từ server
      fetchCart();
    }
  };

  // --- THÊM MỚI: Xử lý xóa sản phẩm ---
  const handleRemoveItem = async (productId) => {
    if (!window.confirm("Bạn có chắc muốn bỏ sản phẩm này khỏi giỏ hàng?")) return;

    try {
      await cartApi.removeFromCart(productId);
      // Xóa khỏi state cartItems
      setCartItems(cartItems.filter(item => item.productId !== productId));
      // Xóa khỏi danh sách đang chọn (nếu có)
      setSelectedItems(selectedItems.filter(id => id !== productId));
    } catch (err) {
      console.error("Lỗi xóa sản phẩm:", err);
      alert("Không thể xóa sản phẩm lúc này!");
    }
  };

  return (
    <div className="container mt-4 mb-5">
      <nav aria-label="breadcrumb">
        <ol className="breadcrumb mb-4">
            <li className="breadcrumb-item"><Link to="/" className="text-danger text-decoration-none">Trang chủ</Link></li>
            <li className="breadcrumb-item active">Giỏ hàng</li>
        </ol>
      </nav>

      <div className="row gap-4">
        <div className="col-md-7">
          <h4 className="mb-4">Giỏ hàng của bạn</h4>
          <div className="bg-white border p-4">
            {cartItems.length > 0 ? (
              <div className="table-responsive">
                <table className="table align-middle text-center mb-0">
                  <thead className="table-light">
                    <tr>
                      <th scope="col" style={{width: '5%'}}>
                        <input type="checkbox" className="form-check-input" checked={selectedItems.length === cartItems.length && cartItems.length > 0} onChange={handleSelectAll} />
                      </th>
                      <th className="text-start">Sản phẩm</th>
                      <th>SL</th>
                      <th>Giá</th>
                      <th>Thành tiền</th>
                      <th>Thao tác</th> 
                    </tr>
                  </thead>
                  <tbody>
                    {cartItems.map((item, idx) => (
                      <tr key={idx}>
                        <td>
                            <input type="checkbox" className="form-check-input" checked={selectedItems.includes(item.productId)} onChange={() => handleSelectOne(item.productId)} />
                        </td>
                        <td className="text-start fw-bold">{item.productName}</td>
                 
                        <td>
                          <div className="input-group input-group-sm mx-auto" style={{ width: "90px", flexWrap: 'nowrap' }}>
                            <button className="btn btn-outline-secondary" onClick={() => handleQuantityChange(item.productId, item.quantity, -1)} disabled={item.quantity <= 1}>-</button>
                            <input type="text" className="form-control text-center px-1" value={item.quantity} readOnly />
                            <button className="btn btn-outline-secondary" onClick={() => handleQuantityChange(item.productId, item.quantity, 1)}>+</button>
                          </div>
                        </td>

                        <td className="text-muted">{item.price?.toLocaleString()}đ</td>
                        <td className="text-danger fw-bold">{(item.price * item.quantity).toLocaleString()}đ</td>
                        <td>
                          <button className="btn btn-sm btn-outline-danger" onClick={() => handleRemoveItem(item.productId)}>
                            Xóa
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            ) : (
              <div className="text-center py-5">
                <div className="mb-3"><span style={{ fontSize: '4rem', color: '#ccc' }}>🛒</span></div>
                <h5 className="fw-bold mb-2">Giỏ hàng trống</h5><p className="text-muted mb-4">Bạn chưa có sản phẩm nào trong giỏ hàng</p>
                <button className="btn btn-danger px-5 py-2 rounded-0 fw-bold" onClick={() => navigate('/')}>Tiếp tục mua sắm</button>
              </div>
            )}
          </div>
        </div>

        <div className="col-md-4">
          <div className="bg-white border p-4">
            <h5 className="mb-4 pb-3 border-bottom">Tổng đơn hàng</h5>
            <div className="d-flex justify-content-between mb-3 text-muted"><span>Tạm tính:</span><span>{calculateTotal().toLocaleString()}đ</span></div>
            <div className="d-flex justify-content-between mb-3 text-muted"><span>Phí vận chuyển:</span><span>0đ</span></div>
            <hr />
            <div className="d-flex justify-content-between mb-4"><h6 className="fw-bold text-danger mb-0">Tổng cộng:</h6><h5 className="fw-bold text-danger mb-0">{calculateTotal().toLocaleString()}đ</h5></div>
            <button className="btn btn-danger w-100 py-2 mb-2 rounded-0 fw-bold" onClick={handleCheckout} disabled={selectedItems.length === 0}>THANH TOÁN</button>
            <button className="btn btn-outline-secondary w-100 py-2 rounded-0" onClick={() => navigate('/')}>TIẾP TỤC MUA SẮM</button>
          </div>
        </div>
      </div>
    </div>
  );
};
export default Cart;