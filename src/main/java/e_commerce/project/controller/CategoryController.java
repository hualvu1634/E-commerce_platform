package e_commerce.project.controller;


import e_commerce.project.dto.response.ApiResponse;
import e_commerce.project.dto.response.PageResponse;
import e_commerce.project.dto.response.ProductResponse;
import e_commerce.project.entity.Category;
import e_commerce.project.service.CategoryService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
   
    @PostMapping
     @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Category> addCategory( @Valid @RequestBody Category request) {
        return ResponseEntity.ok(categoryService.addCategory(request));
    }
    
    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }
    @GetMapping("/{id}/products")
    public ResponseEntity<PageResponse<ProductResponse>> getCategory(@PathVariable Long id,@RequestParam(value = "page",defaultValue = "1") int page){
    int pageSize = 10;
    return ResponseEntity.ok(categoryService.getProductsByCategory(id, page, pageSize));
    }

    //xóa danh mục
    @DeleteMapping("/{id}")
     @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> delCategory(@PathVariable Long id){
      
        return ResponseEntity.ok(categoryService.delCategory(id));

    }
}