package com.example.demo.controler;

import com.example.demo.dto.Hello;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController//DEFINE bbiet controller
@RequestMapping("/api/v1/")//Tạo API tại  đây
public class HelloSpringBoot {

    @GetMapping("/hello")
    public Hello HelloSpring() {
        Hello hello = new Hello("abc", 10);
        return hello;
    }

}
