package e_commerce.project.config;

import e_commerce.project.entity.Category;
import e_commerce.project.entity.Product;
import e_commerce.project.entity.User;
import e_commerce.project.enumerate.Role;
import e_commerce.project.repository.CategoryRepository;
import e_commerce.project.repository.ProductRepository;
import e_commerce.project.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class Initialize {
   
    @Bean
    CommandLineRunner initDatabase(CategoryRepository categoryRepo, 
                                   UserRepository userRepo,
                                   ProductRepository productRepo,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
         
            if (categoryRepo.count() == 0) {
               
                categoryRepo.saveAll(List.of(Category.builder().name("Apple").build(),
                                            Category.builder().name("Samsung").build(),
                                            Category.builder().name("Xiaomi").build()));
            }

      
            if (userRepo.count() == 0) {
                userRepo.saveAll(List.of(User.builder()
                        .username("admin123")
                        .password(passwordEncoder.encode("12345678"))
                        .name("Admin")
                        .phoneNumber("0099999999")
                        .role(Role.ADMIN)
                        .build(), User.builder()
                        .username("user1234")
                        .password(passwordEncoder.encode("12345678"))
                        .name("User")
                        .phoneNumber("0123456789")
                        .role(Role.USER)
                        .build()));
            }
            // Khởi tạo sản phẩm mẫu
            if (productRepo.count() == 0) {
                // Lấy danh mục để gán category_id chính xác
                Category apple = categoryRepo.findByName("Apple").orElse(null);
                Category samsung = categoryRepo.findByName("Samsung").orElse(null);

                productRepo.saveAll(List.of(
                    Product.builder()
                        .name("Iphone 14 Pro Max")
                        .description("8/512Gb")
                        .imageUrl("https://cdn.viettablet.com/images/companies/1/0-hinh-moi/tin-tuc/2022/thang-9/8/apple-iphone-14-pro-max-6.jpg?1662631346182")
                        .price(BigDecimal.valueOf(15000000))
                        .quantity(1000)
                        .isActive(true)
                        .category(apple)
                        .build(),
                    Product.builder()
                        .name("Iphone 16 Plus")
                        .description("8/512GB")
                        .imageUrl("https://cdn2.cellphones.com.vn/358x/media/catalog/product/i/p/iphone-16-plus-xanh-luu-ly.png")
                        .price(BigDecimal.valueOf(25000000))
                        .quantity(1000)
                        .isActive(true)
                        .category(apple)
                        .build(),
                    Product.builder()
                        .name("Samsung S24 Ultra")
                        .description("8/512GB")
                            .price(BigDecimal.valueOf(25000000))
                        .imageUrl("https://cdn2.cellphones.com.vn/358x/media/catalog/product/g/a/galaxy-s24-ultra-den-1_1_3.png")
                        .quantity(1000)
                        .isActive(true)
                        .category(samsung)
                        .build()
                ));
            }
         
        };
    }
}