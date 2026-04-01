package e_commerce.project.dto.response;



import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class OrderItemResponse {
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;       
    private BigDecimal subTotal;    
}