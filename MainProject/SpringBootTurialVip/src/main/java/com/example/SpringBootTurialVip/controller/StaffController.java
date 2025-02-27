package com.example.SpringBootTurialVip.controller;

import com.example.SpringBootTurialVip.dto.request.ApiResponse;
import com.example.SpringBootTurialVip.dto.request.ChildCreationRequest;
import com.example.SpringBootTurialVip.dto.request.ProductRequest;
import com.example.SpringBootTurialVip.dto.response.ChildResponse;
import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.enums.OrderStatus;
import com.example.SpringBootTurialVip.service.CategoryService;
import com.example.SpringBootTurialVip.service.OrderService;
import com.example.SpringBootTurialVip.service.ProductService;
import com.example.SpringBootTurialVip.service.serviceimpl.StaffService;
import com.example.SpringBootTurialVip.service.serviceimpl.UserService;
import com.example.SpringBootTurialVip.entity.Category;
import com.example.SpringBootTurialVip.entity.Product;
import com.example.SpringBootTurialVip.entity.ProductOrder;
import com.example.SpringBootTurialVip.util.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/staff")//do user dùng chung nhiều khai bóa ở đây ở dưới sẽ ko cần
@Slf4j
@RequiredArgsConstructor
@Tag(name="[StaffController]",description = "Cần authen")
public class StaffController {

    /*
    * @ModelAttribute :Tự động ánh xạ dữ liệu từ form vào một đối tượng (Model)
    * @RequestParam	:Lấy dữ liệu từ query string hoặc form input (dạng key-value)
    * @RequestBody	:Đọc dữ liệu từ request body (thường dùng cho API với JSON/XML)
    * ObjectUtils giúp tránh các giá trị null
    */

    private final StaffService staffService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommonUtil commonUtil;

    private final ObjectMapper objectMapper = new ObjectMapper(); // Dùng để chuyển đổi JSON


//    public StaffController(StaffService staffService) {
//        this.staffService = staffService;
//    }

    //API xóa thông tin user
//    @DeleteMapping("/delete/{userId}")
//    String deleteUser(@PathVariable("userId") Long userID){
//        userService.deleteUser(userID);
//        return "Đã delete user và danh sách sau khi delete là : ";
//    }

    //API: Xem danh sách tất cả trẻ
    @Operation(summary = "Xem danh sách tất cả trẻ em")
    @GetMapping("/children")
    public ResponseEntity<List<ChildResponse>> getAllChildren() {
        return ResponseEntity.ok(staffService.getAllChildren());
    }

    //API: Update(Edit) thông tin `Child`
    @Operation(summary = "Update thông tin trẻ dựa theo id của trẻ")
    @PutMapping("/children/{childId}/update")
    public ResponseEntity<ChildResponse> updateChildInfo(
            @PathVariable Long childId,
            @RequestBody ChildCreationRequest request) {
        return ResponseEntity.ok(staffService.updateChildInfo(childId, request));
    }

    //API: Xem danh sách tất cả customer
    @Operation(summary = "Xem danh sách tất cả khách hàng")
    @GetMapping("/parents")
    public ResponseEntity<List<UserResponse>> getAllParents() {
        return ResponseEntity.ok(staffService.getAllParents());
    }

    //API: Tạo child cho 1 customer theo
    @Operation(summary = "Tạo 1 child cho 1 khách hàng = cách gán parentid của trẻ đc tạo = id của khách")
    @PostMapping("/children/create/{parentId}")
    public ResponseEntity<ChildResponse> createChildForParent(
            @PathVariable("parentId") Long parentId,
            @RequestBody ChildCreationRequest request) {
        return ResponseEntity.ok(staffService.createChildForParent(parentId, request));
    }

    //API Update(Edit) customer
    //API khóa tài khoản customer , có thể coi là xóa

    //API : Thêm sản phẩm (gồm hình ảnh (nếu 0 có sẽ default)) và các thuộc tính cần thiết khác )
//    @Operation(summary = "API thêm vaccine")
//    @PostMapping("/addProduct")
//    public ResponseEntity<?> saveProduct(@ModelAttribute Product product,
//                                         @RequestParam("file") MultipartFile image) throws IOException {
//
//        try {
//            String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();
//            product.setImage(imageName);
//            product.setDiscount(0);
//            product.setDiscountPrice(product.getPrice());
//
//            Product savedProduct = productService.addProduct(product);
//
//            if (!ObjectUtils.isEmpty(savedProduct)) {//N
//                File saveFile = new ClassPathResource("/static/img/").getFile();
//                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator
//                        + image.getOriginalFilename());
//                Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//
//                return ResponseEntity.ok(Collections.singletonMap("message", "Product saved successfully"));
//            } else {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .body(Collections.singletonMap("error", "Something went wrong on server"));
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(Collections.singletonMap("error", e.getMessage()));
//        }
//    }
//    @Operation(summary = "API thêm vaccine", description = "Thêm sản phẩm vaccine với hình ảnh")
//    @PostMapping(value = "/addProduct", consumes = {"multipart/form-data"})
//    public ResponseEntity<?> saveProduct(
//            @RequestPart("product") @RequestBody Product product,
//            @RequestPart("file") @Parameter(
//                    description = "File ảnh của sản phẩm",
//                    content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", format = "binary"))
//            ) MultipartFile image) throws IOException {
//
//        // Kiểm tra file có được upload không
//        if (image == null || image.isEmpty()) {
//            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "File is required"));
//        }
//
//        // Xử lý file ảnh
//        String imageName = image.getOriginalFilename();
////        product.setImage(imageName);
//        product.setDiscount(0);
//        product.setDiscountPrice(product.getPrice());
//
//        // Lưu sản phẩm
//        Product savedProduct = productService.addProduct(product);
//        return ResponseEntity.ok(Collections.singletonMap("message", "Product saved successfully"));
//    }


    @Operation(summary = "API thêm vaccine", description = "Thêm sản phẩm vaccine với hình ảnh")
    @PostMapping(value = "/addProduct", consumes = {"multipart/form-data"})
    public ResponseEntity<?> saveProduct(
            @RequestPart("product") String productJson, // Nhận JSON dưới dạng String
            @RequestPart("file") MultipartFile image) throws IOException {

        // Chuyển đổi productJson từ String thành đối tượng Product
        ObjectMapper objectMapper = new ObjectMapper();
        Product product = objectMapper.readValue(productJson, Product.class);

        if (image.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "File is required"));
        }

        // Lưu file ảnh
        String imageName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        product.setImage(imageName);

        // Lưu sản phẩm vào DB
        product.setDiscount(0);
        product.setDiscountPrice(product.getPrice());
        Product savedProduct = productService.addProduct(product);

        return ResponseEntity.ok(Collections.singletonMap("message", "Product saved successfully"));
    }




    //API lấy thông tin tất cả sản phẩm
    @Operation(summary = "API xem danh sách vaccine")
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProduct() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    //API lấy thông tin sản phẩm theo tên
    @Operation(summary = "[BUG] API tìm kiếm sản phẩm theo tên ")
    @GetMapping("/searchProduct")
    public ResponseEntity<ApiResponse<List<Product>>> searchProduct(@RequestParam String title) {
        List<Product> searchProducts = productService.getProductByTitle(title);

        List<Category> categories = categoryService.getAllActiveCategory();

        ApiResponse<List<Product>> response = new ApiResponse<>(1000, "Search results", searchProducts);
        return ResponseEntity.ok(response);
    }



    //API Update(Edit) sản phẩm
    @Operation(summary = "API cập nhật sản phẩm = id sản phẩm ")
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
    @Operation(summary = "API xóa sản phẩm = id sản phẩm ")
    @DeleteMapping("/deleteProduct/{productId}")
    String deleteUser(@PathVariable("productId") Long productId){
        productService.deleteProduct(productId);
        return "Đã delete product ";
    }

    //API tạo Category
    @Operation(summary = "[BUG] API tạo danh mục")
//    @PostMapping("/createCategory")
//    public ResponseEntity<?> saveCategory(@ModelAttribute Category category,
//                                          @RequestParam(value = "file",required = false) MultipartFile file) throws IOException {
//        try {
//            // Xử lý tên ảnh (nếu không có, dùng mặc định)
//            String imageName = (file != null && !file.isEmpty()) ? file.getOriginalFilename() : "default.jpg";
//            category.setImageName(imageName);
//
//            // Kiểm tra nếu danh mục đã tồn tại
//            if (categoryService.existCategory(category.getName())) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                        .body(Collections.singletonMap("error", "Category Name already exists"));
//            }
//
//            // Lưu category vào DB
//            Category savedCategory = categoryService.saveCategory(category);
//
//            if (!ObjectUtils.isEmpty(savedCategory)) {
//                // Lưu file ảnh nếu có
//                if (file != null && !file.isEmpty()) {
//                    File saveFile = new ClassPathResource("static/img/").getFile();
//                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator
//                            + file.getOriginalFilename());
//                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//                }
//                return ResponseEntity.ok(Collections.singletonMap("message", "Category saved successfully"));
//            } else {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .body(Collections.singletonMap("error", "Not saved! Internal server error"));
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(Collections.singletonMap("error", e.getMessage()));
//        }
//    }

    @PostMapping(value = "/createCategory", consumes = {"multipart/form-data"})
    public ResponseEntity<?> saveCategory(
            @RequestPart("category")
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dữ liệu danh mục (JSON)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))
            ) String categoryJson,

            @RequestPart(value = "file", required = false)
            @Parameter(
                    description = "Hình ảnh danh mục (File ảnh)",
                    content = @Content(mediaType = "multipart/form-data")
            ) MultipartFile file) throws IOException {

        try {
            // Chuyển đổi JSON từ String thành đối tượng Category
            Category category = objectMapper.readValue(categoryJson, Category.class);

            // Xử lý tên ảnh (nếu không có, dùng mặc định)
            String imageName = (file != null && !file.isEmpty()) ? file.getOriginalFilename() : "default.jpg";
            category.setImageName(imageName);

            // Kiểm tra nếu danh mục đã tồn tại
            if (categoryService.existCategory(category.getName())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("error", "Category Name already exists"));
            }

            // Lưu category vào DB
            Category savedCategory = categoryService.saveCategory(category);

            if (!ObjectUtils.isEmpty(savedCategory)) {
                // Lưu file ảnh nếu có
                if (file != null && !file.isEmpty()) {
                    File saveFile = new ClassPathResource("static/img/").getFile();
                    Path path = Path.of(saveFile.getAbsolutePath(), "category_img", file.getOriginalFilename());
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                }
                return ResponseEntity.ok(Collections.singletonMap("message", "Category saved successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.singletonMap("error", "Not saved! Internal server error"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Failed to parse JSON: " + e.getMessage()));
        }
    }



    //API lấy tất cả category
    @Operation(summary = "Api hiển thị tất cả danh mục ")
    @GetMapping("/showCategory")
    public ResponseEntity<ApiResponse<List<Category>>> loadAddProduct() {
        List<Category> categories = categoryService.getAllCategory();
        ApiResponse<List<Category>> response = new ApiResponse<>(1000, "Fetched categories successfully", categories);
        return ResponseEntity.ok(response);
    }



    //API lấy tất cả category đang hoạt động
    @Operation(summary = "API hiển thị tất cả các danh mục đang hoạt động")
    @GetMapping("/showActiveCategory")
    public ResponseEntity<ApiResponse<List<Category>>> showActiveCategory() {
        List<Category> categories = categoryService.getAllActiveCategory();
        ApiResponse<List<Category>> response = new ApiResponse<>(1000, "Fetched categories successfully", categories);
        return ResponseEntity.ok(response);
    }

    //API Update(Edit) Category = ID
    @Operation(summary = "API edit danh mục")
    @PutMapping("/updateCategory/{id}")
    public ResponseEntity<ApiResponse<Category>> updateCategory(
            @PathVariable Integer id,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "isActive") Boolean isActive,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        // Tìm Category theo ID
        Category oldCategory = categoryService.getCategoryById(id);
        if (oldCategory == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(1004, "Category not found", null));
        }

        // Cập nhật thông tin category
        String imageName = (file != null && !file.isEmpty()) ? file.getOriginalFilename() : oldCategory.getImageName();
        oldCategory.setName(name);
        oldCategory.setIsActive(isActive);
        oldCategory.setImageName(imageName);

        // Lưu category
        Category updatedCategory = categoryService.saveCategory(oldCategory);

        // Lưu file ảnh nếu có upload mới
        if (file != null && !file.isEmpty()) {
            File saveFile = new ClassPathResource("static/img").getFile();
            Path path = Paths.get(saveFile.getAbsolutePath(), "category_img", file.getOriginalFilename());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }

        return ResponseEntity.ok(new ApiResponse<>(1000, "Category updated successfully", updatedCategory));
    }

    //API Xóa Category = ID
    @Operation(summary = "API xóa danh mục = id ")
    @DeleteMapping("/deleteCategory/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable int id) {
        Boolean isDeleted = categoryService.deleteCategory(id);

        if (isDeleted) {
            return ResponseEntity.ok(new ApiResponse<>(1000, "Category deleted successfully", null));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(1004, "Failed to delete category", null));
        }
    }

    //API tìm Category theo tên
    @Operation(summary = "[Ko cần xài] API tìm kiếm danh muc6 ")
    @GetMapping("/searchCategory")
    public ResponseEntity<ApiResponse<List<Category>>> searchCategory(@RequestParam String name) {
        List<Category> categories = categoryService.findByNameContaining(name);

        if (categories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(1004, "No categories found", null));
        }

        return ResponseEntity.ok(new ApiResponse<>(1000, "Categories found", categories));
    }

    //API cập nhật  tình trạng đơn hàng
    @Operation(summary = "API cập nhật trạng thái đơn hàng = id đơn hàng",description =
            "StatusID list : (1,In Progress) \n"+
                    "(2,Order Received) \n" +
                    "(3, Out for Stock) \n" +
                    "(4,Cancelled) \n" +
                    "(5,Success) \n")
    @PutMapping("/update-status")
    public ResponseEntity<ApiResponse<ProductOrder>> updateOrderStatus(
            @RequestParam Long id,//ID đơn hàng
            @RequestParam Integer statusId) {

        //Tìm `OrderStatus` nhanh hơn bằng Stream API
        Optional<OrderStatus> matchedStatus = Arrays.stream(OrderStatus.values())
                .filter(orderStatus -> orderStatus.getId().equals(statusId))
                .findFirst();

        if (matchedStatus.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(1001, "Invalid status ID", null));
        }

        String status = matchedStatus.get().getName();

        // Cập nhật trạng thái đơn hàng
        ProductOrder updatedOrder = orderService.updateOrderStatus(id, status);

        if (updatedOrder == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(1002, "Order not found or status not updated", null));
        }

        // Gửi email thông báo nếu cần
        try {
            commonUtil.sendMailForProductOrder(updatedOrder, status);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(1003, "Order updated but email failed", updatedOrder));
        }

        return ResponseEntity.ok(new ApiResponse<>(1000, "Order status updated successfully", updatedOrder));
    }

    //API tìm kiếm user
    @Operation(summary = "APi tìm kiếm 1 user = user id ")
    @GetMapping("/{userId}")
        //Nhận 1 param id để tìm thông tin user đó
    UserResponse getUser(@PathVariable("userId") Long userId) {
        return userService.getUserById(userId);
    }

    //API cho xem order

    //API Update trạng thái order

    //APi tìm order theo id










}
