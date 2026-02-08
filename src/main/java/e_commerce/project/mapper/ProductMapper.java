package e_commerce.project.mapper;

import e_commerce.project.dto.request.ProductRequest;
import e_commerce.project.dto.response.ProductResponse;
import e_commerce.project.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse toProductResponse(Product product);
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "isActive", ignore = true) 
    @Mapping(target = "category",ignore = true)
    Product toProduct(ProductRequest request);

    @Mapping(target = "category", ignore = true) 
    @Mapping(target = "isActive", ignore = true) 
    @Mapping(target = "id",ignore = true)
    void updateProduct(@MappingTarget Product product, ProductRequest request);
}