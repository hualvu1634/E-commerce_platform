package e_commerce.project.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import e_commerce.project.entity.Product;

import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product,Long> {

   Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    boolean existsByName(String name);
    Optional<Product> findByName(String productName);
    
}
