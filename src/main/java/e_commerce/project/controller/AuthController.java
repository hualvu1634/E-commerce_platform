package e_commerce.project.controller;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import e_commerce.project.dto.request.LoginRequest;
import e_commerce.project.dto.request.UserRequest;
import e_commerce.project.dto.response.LoginResponse;
import e_commerce.project.dto.response.UserResponse;
import e_commerce.project.service.AuthenticationService;
import e_commerce.project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService; 
    private final UserService userService;
        @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }
        @PostMapping("/register")
    public ResponseEntity<UserResponse> addUser( @Valid @RequestBody UserRequest userRequest){
        return new ResponseEntity<UserResponse>(userService.addAccount(userRequest),HttpStatus.CREATED);
    }
}

