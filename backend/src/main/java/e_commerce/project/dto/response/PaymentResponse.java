package e_commerce.project.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentResponse {
    private Long orderId;
    private boolean success;
    private String message;
}