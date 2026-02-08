package e_commerce.project.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import e_commerce.project.dto.request.ProductRequest;
import e_commerce.project.dto.response.ApiResponse;
import e_commerce.project.dto.response.PageResponse;
import e_commerce.project.dto.response.ProductResponse;
import e_commerce.project.entity.Category;
import e_commerce.project.entity.Product;
import e_commerce.project.enumerate.ErrorCode;
import e_commerce.project.exception.AppException;
import e_commerce.project.mapper.ProductMapper;
import e_commerce.project.repository.CategoryRepository;
import e_commerce.project.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@SuppressWarnings("null")
@RequiredArgsConstructor
public class ProductService {


    private final  ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductResponse addProduct(ProductRequest request) {
        if (productRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }
        Category category = categoryRepository.findById(request.getCategoryId())
        .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        Product product = productMapper.toProduct(request);
        product.setCategory(category);
         productRepository.save(product);

         return productMapper.toProductResponse(product);

    }

public PageResponse<ProductResponse> getAllProducts(int page, int size) {
   
    Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").ascending());


    Page<Product> pageData = productRepository.findAll(pageable);

    List<ProductResponse> responseList = pageData.getContent().stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    return PageResponse.<ProductResponse>builder()
            .currentPage(page)
            .pageSize(pageData.getSize())
            .totalPages(pageData.getTotalPages())
            .totalElements(pageData.getTotalElements())
            .data(responseList) 
            .build();
}

    public ProductResponse getProduct(Long id) {
        Product product= productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
                return productMapper.toProductResponse(product);
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {     
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        productMapper.updateProduct(product, request);
        if (request.getCategoryId() != null) {
            Category newCategory = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
            product.setCategory(newCategory);
        }

        return productMapper.toProductResponse( productRepository.save(product));
    }

    public ApiResponse delProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        product.setIsActive(false);
        productRepository.save(product);
        return ApiResponse.builder()
        .timestamp(LocalDateTime.now())
        .code(200)
        .message("Xóa sản phẩm thành công")
        .build();

    }

}