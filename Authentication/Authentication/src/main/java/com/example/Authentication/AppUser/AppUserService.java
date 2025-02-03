package com.example.Authentication.AppUser;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
//Class này daành cho spring security

@Service
@AllArgsConstructor //constructor cho biến appUserRepository,thông thường sẽ thêm constructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG  ="user with email %s not found!!!";
    private final AppUserRepository appUserRepository;

    //Phương thức này giúp xác định user khi log in
    //Trong trường hợp này Username sẽ là email của user
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format(
                        USER_NOT_FOUND_MSG,email)));
    }
}
