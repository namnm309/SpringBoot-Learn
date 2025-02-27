package com.example.SpringBootTurialVip.service;

import com.example.SpringBootTurialVip.dto.request.AuthenticationRequest;
import com.example.SpringBootTurialVip.dto.request.VerifyTokenRequest;
import com.example.SpringBootTurialVip.dto.response.AuthenticationResponse;
import com.example.SpringBootTurialVip.dto.response.VerifyTokenResponse;

public interface AuthenticationService {

    public AuthenticationResponse authencicate(AuthenticationRequest request);

    public VerifyTokenResponse introspect(VerifyTokenRequest request);
}
