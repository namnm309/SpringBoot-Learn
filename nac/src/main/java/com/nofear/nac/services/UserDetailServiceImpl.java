package com.nofear.nac.services;

import com.nofear.nac.entities.User;
import com.nofear.nac.models.UserDetailsImpl;
import com.nofear.nac.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetailsImpl loadUserByUsername(String username) {

        System.out.println("---------------------- Calling UserDetailsImpl::loadUserByUsername ----------------------");

        Supplier<UsernameNotFoundException> s =
                () -> new UsernameNotFoundException("UserDetailsImpl::loadUserByUsername - Problem during authentication!");

        User user = userRepository.findUserByUsername(username).orElseThrow(s);

        return new UserDetailsImpl(user);
    }
}
