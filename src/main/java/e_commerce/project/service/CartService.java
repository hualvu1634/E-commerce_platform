package e_commerce.project.service;

import e_commerce.project.dto.request.ItemRequest;
import e_commerce.project.dto.response.ApiResponse;
import e_commerce.project.dto.response.CartResponse;
import e_commerce.project.entity.*;
import e_commerce.project.enumerate.ErrorCode;
import e_commerce.project.exception.AppException;
import e_commerce.project.mapper.CartMapper;
import e_commerce.project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service

@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;
    private Cart getCartByUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return cartRepository.findByUserId(user.getId());
             
    }

    public CartResponse addToCart(ItemRequest request) {
        Cart cart = getCartByUser();
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        
        if (product.getIsActive()==false) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());
        
        int newQuantity = request.getQuantity();

        if (cartItem != null) {
            newQuantity += cartItem.getQuantity();
        } else {
          
            cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(0) 
                    .build();
        }

        cartItem.setQuantity(newQuantity);
        CartItem savedItem = cartItemRepository.save(cartItem);
        return cartMapper.toCartResponse(savedItem);
    }

    public List<CartResponse> getCart() {
        Cart cart = getCartByUser();
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        return cartItems.stream()
                .map(cartMapper::toCartResponse)
                .toList();
    }

    public List<CartResponse> updateCart(List<ItemRequest> cartRequest) {
        Cart cart = getCartByUser();
        List<CartItem> currentCartItems = cartItemRepository.findByCartId(cart.getId());
        Map<Long, Integer> requestMap = cartRequest.stream()
                .collect(Collectors.toMap(ItemRequest::getProductId, ItemRequest::getQuantity));

      
        for (CartItem item : currentCartItems) {
            Long productId = item.getProduct().getId();
            if (requestMap.containsKey(productId)) {
                item.setQuantity(requestMap.get(productId));
            }
        }

        cartItemRepository.saveAll(currentCartItems);

        return currentCartItems.stream()
                .map(cartMapper::toCartResponse)
                .toList();
    }

    public ApiResponse removeProductFromCart(Long productId) {
        Cart cart = getCartByUser();
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);
        if(cartItem == null)  throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);

        cartItemRepository.delete(cartItem);
        return ApiResponse.builder()
        .timestamp(LocalDateTime.now())
        .code(200)
        .message("Xóa sản phẩm trong giỏ hàng")
        .build();
    }

}