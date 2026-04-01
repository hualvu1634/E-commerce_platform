package e_commerce.project.service;


import e_commerce.project.dto.request.ItemRequest;
import e_commerce.project.dto.request.OrderRequest;
import e_commerce.project.dto.response.OrderItemResponse; 
import e_commerce.project.dto.response.OrderResponse;
import e_commerce.project.dto.response.PageResponse;
import e_commerce.project.entity.*;
import e_commerce.project.enumerate.ErrorCode;
import e_commerce.project.enumerate.OrderStatus;
import e_commerce.project.exception.AppException;
import e_commerce.project.mapper.OrderMapper;
import e_commerce.project.repository.*;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponse placeOrder(OrderRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<ItemRequest> itemRequests = request.getItemRequests();

      
        Order order = orderMapper.toOrder(request);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        BigDecimal totalMoney = BigDecimal.ZERO;
        List<OrderDetail> orderDetails = new ArrayList<>();
        List<Product> productsToUpdate = new ArrayList<>();

        for (ItemRequest item : itemRequests) {
            Product product = productRepository.findById(item.getProductId()).orElseThrow(()->new AppException(ErrorCode.PRODUCT_NOT_FOUND));

            if (product.getQuantity() < item.getQuantity()) {
                throw new AppException(ErrorCode.NOT_ENOUGH);
            }

            product.setQuantity(product.getQuantity() - item.getQuantity());
            productsToUpdate.add(product);

            BigDecimal lineTotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalMoney = totalMoney.add(lineTotal);

            OrderDetail detail = OrderDetail.builder()
                    .order(order)
                    .product(product)
                    .quantity(item.getQuantity())
                    .price(product.getPrice())
                    .subTotal(lineTotal)
                    .build();
            
            orderDetails.add(detail);
        }

        order.setTotalMoney(totalMoney);
        
        // 4. Lưu DB
        Order savedOrder = orderRepository.save(order);
        for (OrderDetail detail : orderDetails) {
            detail.setOrder(savedOrder);
        }
        orderDetailRepository.saveAll(orderDetails);
        productRepository.saveAll(productsToUpdate);

        savedOrder.setOrderDetails(orderDetails);


        OrderResponse response = orderMapper.toOrderResponse(savedOrder);
        if (response.getItems() != null) {
            for (OrderItemResponse item : response.getItems()) {
              
                BigDecimal subTotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                item.setSubTotal(subTotal);
            }
        }

        return response;
    }
    public PageResponse<OrderResponse> getOrder(int page, int size) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

   
    Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").ascending());


    Page<Order> pageData = orderRepository.findByUser(user,pageable);

    List<OrderResponse> responseList = pageData.getContent().stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    return PageResponse.<OrderResponse>builder()
            .currentPage(page)
            .pageSize(pageData.getSize())
            .totalPages(pageData.getTotalPages())
            .totalElements(pageData.getTotalElements())
            .data(responseList) 
            .build();

    }


}