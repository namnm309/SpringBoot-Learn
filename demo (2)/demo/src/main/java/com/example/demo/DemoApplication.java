package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//Chỉ cho Spring Bootd9a6ay6 là nơi khỏi chạy đầu tiên và để cài đặt every thing
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		//Đây là phương thức để chạy project , có thể coi SpringApplication là 1 IOC Container
		//Câu lệnh này tạo ra Contair và chạy các dependency trong nó
		//Các dependency trong nó còn gọi là Bean
		SpringApplication.run(DemoApplication.class, args);

//		ApplicationContext context = SpringApplication.run(App.class, args);
	}

}
