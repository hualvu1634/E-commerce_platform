package e_commerce.project.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import e_commerce.project.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}