package com.nofear.nac.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer")
//@PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
public class CustomerController {

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER_READ')")
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("GET:: customer controller");
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER_WRITE')")
    public ResponseEntity<String> post() {
        return ResponseEntity.ok("POST:: customer controller");
    }

}
