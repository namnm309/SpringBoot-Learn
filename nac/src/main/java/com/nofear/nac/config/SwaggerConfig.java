package com.nofear.nac.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

//Cấu hình swagger
@Configuration
public class SwaggerConfig {


    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI().info(new Info().title("AuthenticatonDemo").
                version("1.0.0")
                .description("Demo API Authentication!!!")
                .license(new License().name("API License").url("http://domain.vn/license")))
                .servers(List.of(new Server().url("http://localhost:8080").description("Server_test")))
                .components(//cấu hình cho phép nhập token để request
                        new Components()
                                .addSecuritySchemes(
                                        "beareAuth",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")))
//        new @SecurityScheme()
////                name = "Authorization",
//                .type =( SecuritySchemeType.HTTP)
//                .bearerFormat = "JWT"
//                                .scheme("bearer")
//        )))
                .security(List.of(new SecurityRequirement().addList("bearerAuth")));

    }

        @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("api-service-1")
                .pathsToMatch("/**")//vị trí scan package controller
                .build();
    }
}

