package com.nofear.nac.demo;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/staff")
//@PreAuthorize("hasRole('STAFF_WRITE')")
@PreAuthorize("hasAnyRole('STAFF_READ', 'STAFF_WRITE')")
public class StaffController {

    @GetMapping
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("GET:: staff controller");
    }

    @PostMapping
    public ResponseEntity<String> post() {
        return ResponseEntity.ok("POST:: staff controller");
    }
}
