package com.example.SpringBootTurialVip.config;

import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

//Tạo 1 admin khi install application
@Configuration
public class ApplicationInitConfig {

    //Sẽ đc khởi chạy khi application chạy
//    @Bean
//    ApplicationRunner applicationRunner(UserRepository userRepository){
//        return args -> {
//          //Check admin đã tồn tại
//          if (userRepository.findByUsername("admin").isEmpty()) {
//              User user=User.
//          }
//        };
//    }
}
