package com.example.SpringBootTurialVip.repository;

import com.example.SpringBootTurialVip.entity.User;
import org.springframework.data.repository.CrudRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserOptionalRepository extends CrudRepository<User,Long> {
    //Tìm user bằng email để đăng nhập
    Optional<User> findByEmail(String email);

    //Tìm user bằng username
    Optional<User> findByUsername(String username);

    //Tìm user = VerificationCode
     Optional<User> findByVerification_code(String verification_code);


}
