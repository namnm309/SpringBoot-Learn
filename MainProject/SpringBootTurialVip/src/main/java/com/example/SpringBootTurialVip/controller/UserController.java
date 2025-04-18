package com.example.SpringBootTurialVip.controller;

import com.example.SpringBootTurialVip.dto.request.*;
import com.example.SpringBootTurialVip.dto.response.*;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.service.CartService;
import com.example.SpringBootTurialVip.service.OrderService;
import com.example.SpringBootTurialVip.service.serviceimpl.UserService;
import com.example.SpringBootTurialVip.entity.Cart;
import com.example.SpringBootTurialVip.entity.OrderRequest;
import com.example.SpringBootTurialVip.entity.ProductOrder;
import com.example.SpringBootTurialVip.repository.CartRepository;
import com.example.SpringBootTurialVip.util.CommonUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")//do user dùng chung nhiều khai bóa ở đây ở dưới sẽ ko cần
@Slf4j
@Tag(name="UserController",description = "Cần authen")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CommonUtil commonUtil;

    //API xem giỏ hàng - OK
    @Operation(summary = "API xem giỏ hàng , giỏ hàng sẽ đc lưu vào db")
    @GetMapping("/cart")
    public ResponseEntity<?> loadCartPage() {

        UserResponse user = getLoggedInUserDetails();
        List<Cart> carts = cartService.getCartsByUser(user.getId());

        // Chuyển danh sách Cart sang DTO để ẩn thông tin User
        List<CartResponse> cartResponses = carts.stream()
                .map(cart -> new CartResponse(
                        cart.getId(),
                        cart.getProduct(),
                        cart.getQuantity(),
                        cart.getTotalPrice(),
                        cart.getTotalOrderPrice()
                ))
                .collect(Collectors.toList());

        if (carts.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                    "message", "Your cart is empty",
                    "carts", Collections.emptyList(),
                    "totalOrderPrice", 0
            ));
        }

        Double totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();

        return ResponseEntity.ok(Map.of(
                "message", "Cart retrieved successfully",
                "carts", cartResponses,
                "totalOrderPrice", totalOrderPrice
        ));
    }


    //API thêm vaccine vào giỏ hàng - OK
    @Operation(summary = "API thêm sản phẩm vào giỏ hàng và lưu vào db")
    @PostMapping("/addCart")
    public ResponseEntity<ApiResponse<Cart>> addToCart(@RequestParam Long pid, @RequestParam Long uid) {
        Cart savedCart = cartService.saveCart(pid, uid);

        if (ObjectUtils.isEmpty(savedCart)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(1004, "Product add to cart failed", null));
        }

        return ResponseEntity.ok(new ApiResponse<>(1000, "Product added to cart", savedCart));
    }

    //API hiển thị thông tin cá nhân để truy xuất giỏ hàng và ... - OK
    @Operation(summary = "API hiển thị profile")
    private UserResponse getLoggedInUserDetails() {
        UserResponse user = userService.getMyInfo();
        return user;
    }

    //API cập nhật giỏ hàng ( khách hàng thêm hoặc bớt sản phẩm ) , nếu giảm = 0 sẽ xóa cart - OK
    @Operation(summary = "APi updatte giỏ hàng ",
    description = "Turial FE:" +
            "APi nhận 2 param là : sy(action thêm hoặc bớt 1 sp, nếu = 0 sẽ xóa khỏi cart" +
            "cid là cart id " +
            "Anh giúp em xử lý hành động sy như thế này với ạ : thêm là String:'increase' và giảm là 'decrease' nếu giảm còn api sẽ thông báo và xóa item đó ra khỏi cart ")

    @PutMapping("/cart/update-quantity")
    public ResponseEntity<?> updateCartQuantity(@Valid @RequestBody UpdateCartQuantityRequest request) {

        String sy = request.getSy();
        Long cid = request.getCid();

        Cart cart = cartRepository.findById(cid).orElse(null);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Cart item not found"));
        }

        int currentQuantity = cart.getQuantity();

        // Xử lý tăng/giảm số lượng
        if ("increase".equalsIgnoreCase(sy)) {
            cart.setQuantity(currentQuantity + 1);
        } else if ("decrease".equalsIgnoreCase(sy)) {
            if (currentQuantity > 1) {
                cart.setQuantity(currentQuantity - 1);
            } else {
                cartRepository.delete(cart);
                return ResponseEntity.ok(Map.of("message", "Product removed from cart"));
            }
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid operation type"));
        }

        cartRepository.save(cart);
        return ResponseEntity.ok(Map.of("message", "Cart quantity updated successfully", "quantity", cart.getQuantity()));
    }


    //API tạo hồ sơ trẻ em - OK
    @Operation(summary = "API tạo hồ sơ trẻ em , dựa theo token để xđ parent")
    @PostMapping("/child/create")
    ApiResponse<User> createChild(@RequestBody
                                  @Valid
                                  ChildCreationRequest childCreationRequest) {

        ApiResponse<User> apiResponse = new ApiResponse<>();

        apiResponse.setResult(userService.createChild(childCreationRequest));

        return apiResponse;
    }

    //API xem hồ sơ trẻ em ( dựa theo token ) - OK
    @GetMapping("/my-children")
    public ResponseEntity<List<ChildResponse>> getMyChildren() {
        return ResponseEntity.ok(userService.getChildInfo());
    }

    //API cập nhật hồ sơ trẻ em ( dựa theo token ) - Phải cập nhật theo ID
    @PutMapping("/update-my-children")
    public ResponseEntity<List<ChildResponse>> updateMyChildren(@RequestBody ChildCreationRequest request) {
        return ResponseEntity.ok(userService.updateChildrenByParent(request));
    }

    //API Xem thông tin cá nhân - OK
    @Operation(summary = "Cũng là API profile")
    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    //API update thông tin user
    @Operation(summary = "API cập nhật thông tin cá nhân")
    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequest userDetails) {
        // Lấy thông tin từ SecurityContextHolder
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = jwt.getClaim("email"); // Lấy email từ token

        System.out.println("DEBUG: Extracted email from token = " + email);

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User email not found in token");
        }

        // Lấy thông tin người dùng từ database
        User existingUser = userService.getUserByEmail(email);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with email: " + email);
        }

        // Cập nhật thông tin mới (chỉ cho phép cập nhật một số trường)
        existingUser.setPhone(userDetails.getPhone());
        existingUser.setFullname(userDetails.getFullname());
        existingUser.setBod(userDetails.getBod());
        existingUser.setGender(userDetails.getGender());

        // Nếu user muốn đổi password, phải mã hóa
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        // Lưu lại thông tin đã cập nhật
        User updatedUser = userService.updateUser(existingUser);

        return ResponseEntity.ok(updatedUser);
    }


    //API xem cart khách hàng đã thêm vài  dựa theo token truy ra thông tin cá nhân
    @Operation(summary = "API xem giỏ hàng")
    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrderPage() {
        UserResponse user = getLoggedInUserDetails();
        List<Cart> carts = cartService.getCartsByUser(user.getId());

        // Chuyển danh sách Cart sang DTO OrderResponse
        List<OrderResponse> cartResponses = carts.stream()
                .map(cart -> new OrderResponse(
                        cart.getId(),
                        cart.getProduct(),
                        cart.getQuantity(),
                        cart.getTotalPrice(),
                        cart.getTotalOrderPrice()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(1000, "Order details fetched successfully", cartResponses));
    }



    //API lưu đơn hàng , đặt hàng từ cart id
    @Operation(summary = "API này sẽ nhận cartId và tiến hành đặt hàng lưu vào db ")
    @PostMapping("/saveOrder")
    public ResponseEntity<ApiResponse<String>> saveOrder(@RequestParam Long cid, @RequestBody OrderRequest orderRequest) {
        try {
            // Gọi service để lưu đơn hàng (không trả về giá trị)
            orderService.saveOrder(cid, orderRequest);

            return ResponseEntity.ok(new ApiResponse<>(1000, "Order saved successfully", null));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(1004, "Error: Cart ID not found - " + e.getMessage(), null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(1004, "Error: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(1004, "Unexpected error: " + e.getMessage(), null));
        }
    }

    //API xem đơn hàng đã đặt
    @Operation(summary = "APi xem đơn hàng đã đặt ")
    @GetMapping("/user-orders")
    public ResponseEntity<ApiResponse<List<ProductOrderResponse>>> myOrder() {
        UserResponse loginUser = getLoggedInUserDetails();
        List<ProductOrder> orders = orderService.getOrdersByUser(loginUser.getId());

        if (loginUser == null || loginUser.getId() == null) {
            log.error("Failed to retrieve logged-in user or user ID is null.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(1001, "Unauthorized: Cannot retrieve user", null));
        }


        // Chuyển đổi danh sách `ProductOrder` sang `ProductOrderResponse` để ẩn thông tin nhạy cảm
        List<ProductOrderResponse> orderResponses = orders.stream()
                .map(order -> new ProductOrderResponse(
                        order.getOrderId(),
                        order.getOrderDate(),
                        order.getProduct(),
                        order.getPrice(),
                        order.getQuantity(),
                        order.getStatus(),
                        order.getPaymentType(),
                        order.getOrderDetail() // Gửi thông tin OrderDetail nếu cần
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(1000, "User orders retrieved successfully", orderResponses));
    }
    //============================================================================================================================







    //API lấy thông tin 1 user
//        @GetMapping("/{userId}")//Nhận 1 param id để tìm thông tin user đó
//        User getUser(@PathVariable("userId") String userId){
//            return userService.getUserById(userId);
//        }



//    @GetMapping("/username/{username}")
//    Optional<User> getUserName(@PathVariable("username") String username){
//        return userService.getUserName(username);
//    }
}