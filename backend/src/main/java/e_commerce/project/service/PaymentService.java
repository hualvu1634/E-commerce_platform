package e_commerce.project.service;

import e_commerce.project.dto.request.PaymentRequest;
import e_commerce.project.dto.response.PaymentResponse;
import e_commerce.project.entity.Order;
import e_commerce.project.enumerate.OrderStatus;
import e_commerce.project.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;

    @Transactional
    public PaymentResponse processSePayWebhook(PaymentRequest request) {
        String content = request.getTransactionContent();
        BigDecimal amountIn = request.getAmountIn();

        // 1. Kiểm tra dữ liệu đầu vào
        if (content == null || amountIn == null || amountIn.compareTo(BigDecimal.ZERO) <= 0) {
            return PaymentResponse.builder()
                    .success(false)
                    .message("Dữ liệu webhook không hợp lệ").build();
        }

        Long orderId = extractOrderId(content);

        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return PaymentResponse.builder()
                    .orderId(orderId)
                    .success(false)
                    .message("Đơn hàng không tồn tại").build();
        }


        if (order.getStatus() == OrderStatus.PENDING) {
    
            BigDecimal testAmount = new BigDecimal("2000"); 
            
            if (amountIn.compareTo(testAmount) >= 0) { 
                order.setStatus(OrderStatus.SUCCESS); 
                orderRepository.save(order);
                return PaymentResponse.builder()
                        .orderId(orderId)
                        .success(true)
                        .message("Thanh toán thành công").build();
            } else {
                return PaymentResponse.builder()
                        .orderId(orderId)
                        .success(false)
                        .message("Số tiền chuyển không đủ").build();
            }
        }

        return PaymentResponse.builder()
                .orderId(orderId)
                .success(false)
                .message("Đơn hàng đã được xử lý trước đó").build();
    }

    private Long extractOrderId(String content) {

        Pattern pattern = Pattern.compile("DH(\\d+)");
        Matcher matcher = pattern.matcher(content.toUpperCase());
        if (matcher.find()) {
            try {
                return Long.parseLong(matcher.group(1)); 
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}