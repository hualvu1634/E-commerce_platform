package e_commerce.project.dto.response;

import java.time.LocalDateTime;

import e_commerce.project.enumerate.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginResponse {
    private String AccessToken;
    private LocalDateTime issuedAt; 
    private Role role;
}
