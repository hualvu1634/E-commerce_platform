package e_commerce.project.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import e_commerce.project.entity.Order;
import e_commerce.project.entity.User;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByUser(User user, Pageable pageable);
 
}