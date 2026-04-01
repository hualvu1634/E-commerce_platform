package e_commerce.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import e_commerce.project.entity.User;
@Repository
public interface UserRepository extends JpaRepository<User,Long>{
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    boolean existsByPhoneNumber(String phoneNumber);
    
}
