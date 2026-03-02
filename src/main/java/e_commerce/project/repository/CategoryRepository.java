package e_commerce.project.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import e_commerce.project.entity.Category;



@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    
    boolean existsByName(String name);

    Optional<Category> findByName(String string);
    
}
