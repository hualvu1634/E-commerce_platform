package e_commerce.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import e_commerce.project.entity.CartItem;

import java.util.List;


public interface CartItemRepository extends JpaRepository<CartItem, Long> { 
    CartItem findByCartIdAndProductId(Long cartId, Long productId);
    List<CartItem> findByCartId(Long cartId);
    
}