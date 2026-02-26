package e_commerce.project.service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
@RequiredArgsConstructor
public class CategoryService {

   
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;



    public Category addCategory(Category request) {
        
        if (categoryRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }
        Category category = Category.builder().name(request.getName()).build(); 
        return categoryRepository.save(category);
    }
    
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

   public PageResponse<ProductResponse> getProductsByCategory(Long categoryId, int page, int size) {
 
    if (!categoryRepository.existsById(categoryId)) {
        throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
    }

    Pageable pageable = PageRequest.of(page - 1, size, Sort.by( "id").ascending());

  
    Page<Product> pageData = productRepository.findByCategoryId(categoryId, pageable);
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



    public ApiResponse delCategory(Long id){
        if(!categoryRepository.existsById(id)) throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        categoryRepository.deleteById(id);
        return ApiResponse.builder()
        .timestamp(LocalDateTime.now())
        .code(200)
        .message("Xóa danh mục thành công")
        .build();
    }
}