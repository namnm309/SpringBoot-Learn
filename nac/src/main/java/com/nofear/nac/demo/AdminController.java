package com.nofear.nac.demo;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasAnyRole('ADMIN_READ', 'ADMIN_WRITE')")
public class AdminController {

    @GetMapping
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("GET:: admin controller");
    }

    @PostMapping
    public ResponseEntity<String> post() {
        return ResponseEntity.ok("POST:: admin controller");
    }


}
