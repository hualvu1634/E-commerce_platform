package e_commerce.project.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import e_commerce.project.entity.Cart;


public interface CartRepository extends JpaRepository<Cart, Long> {
 
    Cart findByUserId(Long userId);
}