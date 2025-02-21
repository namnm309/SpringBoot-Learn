package com.example.SpringBootTurialVip.controller;

import com.example.SpringBootTurialVip.dto.request.ApiResponse;
import com.example.SpringBootTurialVip.dto.request.ChildCreationRequest;
import com.example.SpringBootTurialVip.dto.response.ChildResponse;
import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.service.CategoryService;
import com.example.SpringBootTurialVip.service.ProductService;
import com.example.SpringBootTurialVip.service.serviceimpl.StaffService;
import com.example.SpringBootTurialVip.shopentity.Category;
import com.example.SpringBootTurialVip.shopentity.Product;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/staff")//do user dùng chung nhiều khai bóa ở đây ở dưới sẽ ko cần
@Slf4j
@RequiredArgsConstructor
public class StaffController {

    /*
    * @ModelAttribute :Tự động ánh xạ dữ liệu từ form vào một đối tượng (Model)
    * @RequestParam	:Lấy dữ liệu từ query string hoặc form input (dạng key-value)
    * @RequestBody	:Đọc dữ liệu từ request body (thường dùng cho API với JSON/XML)
    */

    private final StaffService staffService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

//    public StaffController(StaffService staffService) {
//        this.staffService = staffService;
//    }

    //API: Xem danh sách tất cả trẻ
    @GetMapping("/children")
    public ResponseEntity<List<ChildResponse>> getAllChildren() {
        return ResponseEntity.ok(staffService.getAllChildren());
    }

    //API: Update(Edit) thông tin `Child`
    @PutMapping("/children/{childId}/update")
    public ResponseEntity<ChildResponse> updateChildInfo(
            @PathVariable Long childId,
            @RequestBody ChildCreationRequest request) {
        return ResponseEntity.ok(staffService.updateChildInfo(childId, request));
    }

    //API: Xem danh sách tất cả customer
    @GetMapping("/parents")
    public ResponseEntity<List<UserResponse>> getAllParents() {
        return ResponseEntity.ok(staffService.getAllParents());
    }

    //API: Tạo child cho 1 customer theo
    @PostMapping("/children/create/{parentId}")
    public ResponseEntity<ChildResponse> createChildForParent(
            @PathVariable("parentId") Long parentId,
            @RequestBody ChildCreationRequest request) {
        return ResponseEntity.ok(staffService.createChildForParent(parentId, request));
    }

    //API Update(Edit) customer
    //API khóa tài khoản customer , có thể coi là xóa

    //API : Thêm sản phẩm (gồm hình ảnh (nếu 0 có sẽ default)) và các thuộc tính cần thiết khác )
    @PostMapping("/addProduct")
    public ResponseEntity<?> saveProduct(@ModelAttribute Product product,
                                         @RequestParam("file") MultipartFile image) throws IOException {

        try {
            String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();
            product.setImage(imageName);
            product.setDiscount(0);
            product.setDiscountPrice(product.getPrice());

            Product savedProduct = productService.addProduct(product);

            if (!ObjectUtils.isEmpty(savedProduct)) {
                File saveFile = new ClassPathResource("/static/img/").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator
                        + image.getOriginalFilename());
                Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                return ResponseEntity.ok(Collections.singletonMap("message", "Product saved successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.singletonMap("error", "Something went wrong on server"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    //API lấy thông tin tất cả sản phẩm
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProduct() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    //API lấy thông tin sản phẩm theo tên

    @GetMapping("/searchProduct")
    public ResponseEntity<ApiResponse<List<Product>>> searchProduct(@RequestParam String title) {
        List<Product> searchProducts = productService.getProductByTitle(title);

        List<Category> categories = categoryService.getAllActiveCategory();

        ApiResponse<List<Product>> response = new ApiResponse<>(1000, "Search results", searchProducts);
        return ResponseEntity.ok(response);
    }



    //API Update(Edit) sản phẩm
    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable Integer id,
            @ModelAttribute Product product,
            @RequestParam(value = "file", required = false) MultipartFile image) {
        try {
            // Cập nhật sản phẩm
            Product updatedProduct = productService.updateProduct(product, image);
            return ResponseEntity.ok(new ApiResponse<>(1000, "Product updated successfully", updatedProduct));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(1004, e.getMessage(), null));

        }
    }

    //API Xóa sản phẩm
    //API tìm sản phẩm theo tên(title)

    //API tạo Category
    //API lấy tất cả category
    //API Update(Edit) Category
    //API Xóa Category
    //API tìm Category theo tên

    //API cho xem order
    //API Update trạng thái order
    //APi tìm order theo id










}
