package com.auth.setup;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;

@RequiredArgsConstructor
@RestController
@RequestMapping("auth")
public class AuthResource {

    private final KeycloakRestService restService;


    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public String login(String username, String password) {
        return restService.login(username, password);
    }

    @GetMapping
    public Jwt getToken(@AuthenticationPrincipal Jwt jwt) {

        return jwt;
    }
}
