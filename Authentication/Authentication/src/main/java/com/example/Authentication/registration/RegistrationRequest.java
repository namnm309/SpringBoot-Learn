package com.example.Authentication.registration;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

//Sử dụng class này để capture ( bắt ) lấy các thông tin của user
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString

public class RegistrationRequest
{
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;

}
