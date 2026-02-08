package e_commerce.project.dto.request;

import java.math.BigDecimal;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    
    @NotBlank(message = "NOT_NULL")
    @Size(max = 50, message = "INVALID_SIZE") 
    private String name;

    @Size(max = 250, message = "INVALID_SIZE")
    private String description;
    
    @Min(value = 0, message = "INVALID_MIN")
    private BigDecimal price;
 
    @Min(value = 0, message = "INVALID_MIN")
    private Integer quantity; 
    
    @NotNull(message = "NOT_NULL")
    private Long categoryId;
}