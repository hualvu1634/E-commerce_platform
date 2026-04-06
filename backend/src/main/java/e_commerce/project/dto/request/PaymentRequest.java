package e_commerce.project.dto.request;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentRequest {
    
    private BigDecimal amountIn; 
    
    private String transactionContent; 
    
}