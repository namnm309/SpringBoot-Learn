package com.nofear.nac.services;

import com.nofear.nac.entities.Authority;
import com.nofear.nac.entities.Token;
import com.nofear.nac.entities.User;
import com.nofear.nac.password.BCryptPassword;
import com.nofear.nac.repositories.AuthorityRepository;
import com.nofear.nac.repositories.TokenRepository;
import com.nofear.nac.repositories.UserRepository;
import com.nofear.nac.requests.LoginRequest;
import com.nofear.nac.requests.RegisterRequest;
import com.nofear.nac.responses.LoginResponse;

import com.nofear.nac.responses.RegisterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class LoginServiceImpl implements  LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTServiceImpl jwtService;

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private BCryptPassword bCryptPassword;

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public RegisterResponse register(RegisterRequest request) {

        if(request.getUsername() == null || request.getPassword() == null) {
            return new RegisterResponse("Invalid Request");
        }

        Supplier<UsernameNotFoundException> authorityException =
                () -> new UsernameNotFoundException("Problem during find customer role!");

        Authority authorityCustomerRead = authorityRepository.findUserByAuthority("CUSTOMER_READ").orElseThrow(authorityException);
        Authority authorityCustomerWrite = authorityRepository.findUserByAuthority("CUSTOMER_WRITE").orElseThrow(authorityException);

        Optional<User> optionUser = null;

        optionUser = userRepository.findUserByUsername(request.getUsername());

        if(optionUser.isPresent()) {
            return new RegisterResponse("User has already exist");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(bCryptPassword.encode(request.getPassword()));
        user.getAuthorities().add(authorityCustomerRead);
        user.getAuthorities().add(authorityCustomerWrite);

        try {
            userRepository.save(user);
        } catch (Exception e) {
            return new RegisterResponse("Error when crate user " + request.getUsername());
        }

        return new RegisterResponse(request.getUsername()  + " created");
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        System.out.println("-------------- Calling LoginServiceImpl " + request + " --------------");

        try {
             authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
        } catch (Exception e) {
            return new LoginResponse("Login failed");
        }

        String jwtToken =  jwtService.generateToken(request.getUsername());

        User user = userRepository.findUserByUsername(request.getUsername()).orElseThrow();
        saveUserToken(user, jwtToken);

        return new LoginResponse(jwtToken);
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = new Token();
        token.setToken(jwtToken);
        token.setExpired(false);
        token.setUser(user);
        tokenRepository.save(token);
    }
}
