package e_commerce.project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    
    @NotBlank(message = "NOT_NULL")
    @Size(min = 8, max = 25, message = "INVALID_SIZE")
    private String username;

    @NotBlank(message = "NOT_NULL")
    @Size(min = 8, max = 25, message = "INVALID_SIZE")
    private String password;
}