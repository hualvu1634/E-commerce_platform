package e_commerce.project.config;

import e_commerce.project.entity.Category;
import e_commerce.project.entity.User;
import e_commerce.project.enumerate.Role;
import e_commerce.project.repository.CategoryRepository;
import e_commerce.project.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class Initialize {
   
    @Bean
    CommandLineRunner initDatabase(CategoryRepository categoryRepo, 
                                   UserRepository userRepo,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
         
            if (categoryRepo.count() == 0) {
               
                categoryRepo.saveAll(List.of(Category.builder().name("Apple").build(),
                                            Category.builder().name("Samsung").build(),
                                            Category.builder().name("Xiaomi").build()));
            }

      
            if (userRepo.count() == 0) {
                userRepo.save(User.builder()
                        .username("admin123")
                        .password(passwordEncoder.encode("12345678"))
                        .name("Admin")
                        .phoneNumber("0099999999")
                        .role(Role.ADMIN)
                        .build());
            }
            
         
        };
    }
}