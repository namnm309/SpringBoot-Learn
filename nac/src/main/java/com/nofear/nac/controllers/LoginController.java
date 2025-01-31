package com.nofear.nac.controllers;

import com.nofear.nac.requests.LoginRequest;
import com.nofear.nac.requests.RegisterRequest;
import com.nofear.nac.responses.LoginResponse;
import com.nofear.nac.responses.RegisterResponse;
import com.nofear.nac.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request ) {
        return ResponseEntity.ok(loginService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(loginService.login(request));
    }
}
