package e_commerce.project.dto.request;


import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {
    
    @NotBlank(message = "NOT_NULL")
    @Size(max = 50, message = "INVALID_SIZE")
    private String customerName;

    @NotBlank(message = "NOT_NULL")
    @Size(max = 100, message = "INVALID_SIZE")
    private String address;

    @NotBlank(message = "NOT_NULL")
    @Pattern(regexp = "^\\d{10}$", message = "INVALID_PHONE")
    private String phoneNumber;
    @NotEmpty(message = "NOT_NULL")
    List<ItemRequest> itemRequests;
}