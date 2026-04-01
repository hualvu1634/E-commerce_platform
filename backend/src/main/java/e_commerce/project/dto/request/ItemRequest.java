package e_commerce.project.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemRequest {
    
    @NotNull(message = "NOT_NULL")
    private Long productId;
    
    @NotNull(message = "NOT_NULL")
    @Min(value = 1, message = "INVALID_MIN")
    private Integer quantity;
}