package com.nofear.nac.services;

import com.nofear.nac.requests.LoginRequest;
import com.nofear.nac.requests.RegisterRequest;
import com.nofear.nac.responses.LoginResponse;
import com.nofear.nac.responses.RegisterResponse;

public interface LoginService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
