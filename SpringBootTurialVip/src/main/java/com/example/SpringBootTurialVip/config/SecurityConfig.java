package com.example.SpringBootTurialVip.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration//init và run các public method có @Bean vào ApllicationContext(IOC Container)
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.signerKey}")
    private String signerKey;

    //Tạo biến để cho phép các endpoint
    private final String [] PUBLIC_ENDPOINT={"auth/loginToken"
            ,"auth/verifyToken"
            ,"users/createUser"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        //Cấu hình endpoint nào cần bảo vệ và endpoint nào ko cần
        //Cụ thể : sign up user , log in page ,...
        httpSecurity.authorizeHttpRequests(request -> //lambda function
                request.requestMatchers(HttpMethod.POST,PUBLIC_ENDPOINT)
                        .permitAll()

                        .anyRequest().authenticated());//Bước này vân sẽ chưa truy cập đc do csrf đc auto bật lên để protect => disable

        //Sử dụng token để truy cập , sử dụng o2auth
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer
                        //.jwkSetUri() , dùng cho bên thứ 3
                                .decoder(jwtDecoder())//cần implement
                        ));


        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    //Method này check token có hợp lệ kjo để cho phép truy cập
    @Bean
    JwtDecoder jwtDecoder(){
        SecretKeySpec secretKeySpec=new SecretKeySpec(signerKey.getBytes(),"HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }
}
