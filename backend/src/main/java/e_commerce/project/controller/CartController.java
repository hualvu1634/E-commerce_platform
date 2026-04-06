package e_commerce.project.controller;
import e_commerce.project.dto.request.ItemRequest;
import e_commerce.project.dto.response.ApiResponse;
import e_commerce.project.dto.response.CartResponse;
import e_commerce.project.service.CartService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/carts")
@CrossOrigin(origins = "http://localhost:5173")
 @PreAuthorize("hasAuthority('USER')")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<CartResponse> addToCart( @Valid @RequestBody ItemRequest request) {
        return new ResponseEntity<>(cartService.addToCart(request),HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<CartResponse>> getCart(){
        return ResponseEntity.ok(cartService.getCart());
    }
    @PutMapping
    public ResponseEntity<List<CartResponse>> updateCart( @Valid @RequestBody List<ItemRequest> cartRequest){
        return ResponseEntity.ok(cartService.updateCart(cartRequest));
    }
    @DeleteMapping("/item/{productId}")
    public ResponseEntity<ApiResponse> removeProductFromCart(@PathVariable Long productId) {
        
            return ResponseEntity.ok(cartService.removeProductFromCart(productId));
        
    }
    
    
}