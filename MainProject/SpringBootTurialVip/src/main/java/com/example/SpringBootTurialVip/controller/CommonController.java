package com.example.SpringBootTurialVip.controller;

import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.service.serviceimpl.UserService;
import com.example.SpringBootTurialVip.util.CommonUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/home")
public class CommonController {

    @Autowired
    private UserService userService;

    @Autowired
    private CommonUtil commonUtil;

    //APi quên mật khẩu => gửi về mail link để đăng nhập lại mật khẩu
    @PostMapping("/forgot-password")
    public ResponseEntity<?> processForgotPassword(//@RequestParam String email,
                                                   HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException {

        UserResponse userByEmail = userService.getMyInfo();

        if (ObjectUtils.isEmpty(userByEmail)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Invalid email"));
        }

        String resetToken = UUID.randomUUID().toString();
        userService.updateUserResetToken(email, resetToken);

        String url = CommonUtil.generateUrl(request) + "/reset-password?token=" + resetToken;

        Boolean sendMail = commonUtil.sendMail(url, email);

        if (sendMail) {
            return ResponseEntity.ok(Map.of("message", "Password reset link has been sent to your email"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Something went wrong! Email not sent"));
        }
    }

}
