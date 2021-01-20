package com.auth.setup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.client.RestTemplate;

import java.util.function.Supplier;

@SpringBootApplication
public class AuthSetupApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthSetupApplication.class, args);
    }
    @Bean
    Supplier<String> tenant() {
        return TenantResolver::resolve;
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

   /* @Bean
    public JwtDecoder jwtDecoder() {
        return new JwtDecoder() {
            @Override
            public Jwt decode(String token) throws JwtException {
                return null;
            }
        };
    }*/
}
