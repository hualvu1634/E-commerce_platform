package e_commerce.project.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Integer quantity; // Số lượng muốn mua
    
@ManyToOne
@JoinColumn(name = "cart_id",nullable = false)
private Cart cart;

@ManyToOne
@JoinColumn(name = "product_id",nullable = false)

private Product product;
}
