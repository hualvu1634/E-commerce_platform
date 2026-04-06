package e_commerce.project.controller;

import e_commerce.project.dto.request.PaymentRequest;
import e_commerce.project.dto.response.PaymentResponse;
import e_commerce.project.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") 
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/sepay-webhook")
    public ResponseEntity<PaymentResponse> handleSePayWebhook(
            @RequestBody PaymentRequest request) {
        
        PaymentResponse response = paymentService.processSePayWebhook(request);
        return ResponseEntity.ok(response);
    }
}