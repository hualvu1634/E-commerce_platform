package e_commerce.project.mapper;

import e_commerce.project.dto.request.OrderRequest;
import e_commerce.project.dto.response.OrderItemResponse;
import e_commerce.project.dto.response.OrderResponse;
import e_commerce.project.entity.Order;
import e_commerce.project.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "totalMoney", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "orderDetails", ignore = true)
    @Mapping(target = "status",ignore = true)
    Order toOrder(OrderRequest request);


    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "orderDetails", target = "items")
    OrderResponse toOrderResponse(Order order);


    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(target = "subTotal", ignore = true) 
    OrderItemResponse toOrderItem(OrderDetail orderDetail);


    
}