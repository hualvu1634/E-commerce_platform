package e_commerce.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.List;

import e_commerce.project.enumerate.OrderStatus;


@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "customerName", nullable = false,length = 100)
    private String customerName;     
    @Column(name = "address", nullable = false,length = 100)
    private String address;      

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;  

    @Column(name = "total_money")
    private BigDecimal totalMoney;    

    @Column(name = "order_date")
    private LocalDateTime orderDate;      
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
   
    private User user;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;
}