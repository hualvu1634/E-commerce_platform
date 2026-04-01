package e_commerce.project.mapper;

import e_commerce.project.dto.response.CartResponse;
import e_commerce.project.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring") 
public interface CartMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.price", target = "price")
    @Mapping(source = "quantity", target = "quantity")
    CartResponse toCartResponse(CartItem cartItem);
    List<CartResponse> toListCartResponse(List<CartItem> cartItems);
}