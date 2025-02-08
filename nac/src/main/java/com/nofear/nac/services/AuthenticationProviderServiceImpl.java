package com.nofear.nac.services;

import com.nofear.nac.models.UserDetailsImpl;
import com.nofear.nac.password.BCryptPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationProviderServiceImpl implements AuthenticationProvider {

    //Tao object de lay thong tin cua user
    @Autowired
    private UserDetailsService userDetailsService;

    //Ma hoa mat khau
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        System.out.println("---------------------- Calling AuthenticationProviderServiceImpl ----------------------");

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails user = userDetailsService.loadUserByUsername(username);
        return checkPassword(user, password, bCryptPasswordEncoder);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass);
    }

    //Tao phuong thuc check mat khau
    private Authentication checkPassword(UserDetails user, String rawPassword, PasswordEncoder encoder) {
        System.out.println("----------- Check Pass : " + rawPassword + ":" +user.getPassword() );
        if (encoder.matches(rawPassword, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
        } else {
            throw new BadCredentialsException("Bad credentials");
        }
    }
}
