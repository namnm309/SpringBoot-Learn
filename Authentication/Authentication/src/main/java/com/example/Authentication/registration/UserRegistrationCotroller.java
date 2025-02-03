package com.example.Authentication.registration;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController//Khai báo đây là lớp controller luồng registration
@RequestMapping(path="api/v1/registration")
@AllArgsConstructor
public class UserRegistrationCotroller {

    private RegistrationService registrationService;

    //Tạo method đăng ký
    public String register(@RequestBody RegistrationRequest request) {//Tạo RegistrationReuquest để capture từ RequestBody
        return registrationService.register(request);//Tạo registrationService
    }
}
