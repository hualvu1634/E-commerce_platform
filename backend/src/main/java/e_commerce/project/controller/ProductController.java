package e_commerce.project.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import e_commerce.project.dto.request.ProductRequest;
import e_commerce.project.dto.response.ApiResponse;
import e_commerce.project.dto.response.PageResponse;
import e_commerce.project.dto.response.ProductResponse;
import e_commerce.project.service.ProductService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/products")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {
    @Autowired
    private ProductService productService;
        @PostMapping //tạo sản phẩm
         @PreAuthorize("hasAuthority('ADMIN')")
        public ResponseEntity<ProductResponse> addProduct( @Valid @RequestBody ProductRequest productRequest){
            return new ResponseEntity<>(productService.addProduct(productRequest),HttpStatus.CREATED);
        }
        @GetMapping
        public ResponseEntity<PageResponse<ProductResponse>> getAllProducts(
        @RequestParam(value = "page", defaultValue = "1") int page) {
        int pageSize = 10; 
        return ResponseEntity.ok(productService.getAllProducts(page, pageSize));
        }

        @GetMapping("/{id}")//xem sản phẩm
        public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id){
            return ResponseEntity.ok(productService.getProduct(id));
        }
        @PutMapping("/{id}")
         @PreAuthorize("hasAuthority('ADMIN')")
        public ResponseEntity<ProductResponse> updateProduct(@PathVariable("id") Long id, @Valid @RequestBody ProductRequest productRequest){
                return ResponseEntity.ok(productService.updateProduct(id, productRequest));
        }
        @DeleteMapping("/{id}")
         @PreAuthorize("hasAuthority('ADMIN')")
        public ResponseEntity<ApiResponse> delProduct(@PathVariable Long id){
    
            return  ResponseEntity.ok(productService.delProduct(id));
        }
    
}

