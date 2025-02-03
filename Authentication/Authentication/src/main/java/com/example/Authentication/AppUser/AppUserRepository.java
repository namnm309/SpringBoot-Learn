package com.example.Authentication.AppUser;


import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;//Lưu ý ko sử dụng thư viện jakarta cũ sẽ ko có phương thức readOnly

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AppUserRepository {
    Optional<appUser> findByEmail(String email);
}
