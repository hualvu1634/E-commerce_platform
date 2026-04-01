package e_commerce.project.service;



import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import e_commerce.project.dto.request.LoginRequest;
import e_commerce.project.dto.response.LoginResponse;
import e_commerce.project.entity.User;
import e_commerce.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserRepository userRepository;

    
    public LoginResponse login(LoginRequest request){
         Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if (authentication.isAuthenticated()) {
            User user =userRepository.findByUsername(request.getUsername()).orElse(null);
            return LoginResponse.builder()
                    .AccessToken(jwtService.generateToken(request.getUsername()))
                    .issuedAt(LocalDateTime.now())
                    .role(user.getRole())
                    .build();
        
    }
    return null;
}
}

