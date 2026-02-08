package e_commerce.project.controller;

import e_commerce.project.dto.request.OrderRequest;
import e_commerce.project.dto.response.OrderResponse;
import e_commerce.project.service.OrderService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/orders")
  @PreAuthorize("hasAuthority('USER')")
public class OrderController {

    @Autowired
    private OrderService orderService;

   
    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(@Valid @RequestBody OrderRequest request) {
        OrderResponse response = orderService.placeOrder(request);
        return ResponseEntity.ok(response);
    }
  
    
}