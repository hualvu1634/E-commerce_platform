package e_commerce.project.dto.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price; 
    private String imageUrl;
}
