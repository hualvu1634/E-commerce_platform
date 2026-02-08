package e_commerce.project.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import e_commerce.project.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
 
}