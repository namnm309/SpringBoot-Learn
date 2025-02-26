package com.example.SpringBootTurialVip.controller;

import com.example.SpringBootTurialVip.dto.request.*;
import com.example.SpringBootTurialVip.dto.response.AuthenticationResponse;
import com.example.SpringBootTurialVip.dto.response.VerifyTokenResponse;
import com.example.SpringBootTurialVip.service.serviceimpl.AuthenticationServiceImpl;
import com.example.SpringBootTurialVip.service.serviceimpl.UserService;
import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Map;

//Class này verify user cung cấp password đã mã hóa có đúng ko
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor//Autowired các bean
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)//ko khai báo thì các field sẽ đc override thành private và final
@Tag(name="[Authentication API]",description = "(Ko cần authen) Các api về xác thực và đăng nhập ")
public class AuthenticationController {
    //Tạo 2 dto
    //dto AuthenticationRequest để user cung cấp username và passworrd để log in
    // dto AuthenticationResponse để xem user có cung cấp đúng tk mk ko

    //Tạo AuthenticationService
    @Autowired
    AuthenticationServiceImpl authenticationServiceImpl;

    @Autowired
    UserService userService;

    //API login
    @Operation(summary = "API đăng nhập")
    @PostMapping("/loginToken")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest){
        var result = authenticationServiceImpl.authencicate(authenticationRequest);

        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    //API xác thực token ( chỉ test )
    @Operation(summary = "[Không Xài]API phục vụ mục đích test token")
    @PostMapping("/verifyToken")
    ApiResponse<Void> authenticate(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationServiceImpl.logout(request);

        return ApiResponse.<Void>builder()

                .build();

    }

    //API Verify account để log in
    @Operation(summary = "API xác thực tài khoản sau bước đăng ký ",
    description = "API đăng ký nằm ở Common Controller")
    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody
                                        @Valid
                                        VerifyAccountRequest verifyAccountRequest) {
        try {
            userService.verifyUser(verifyAccountRequest);
            return ResponseEntity.ok("Account verified successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //API logout
    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationServiceImpl.logout(request);
        return ApiResponse.<Void>builder().build();
    }






}
