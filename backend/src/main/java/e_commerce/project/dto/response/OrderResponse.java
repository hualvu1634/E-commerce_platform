package e_commerce.project.dto.response;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.List;

import e_commerce.project.enumerate.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder

public class OrderResponse {
    private Long orderId;
    private Long userId;
    private String customerName; 
    private String address;     
    private String phoneNumber;  
    private LocalDateTime orderDate;
    private BigDecimal totalMoney;   
    private List<OrderItemResponse> items;
    private OrderStatus status;
}
