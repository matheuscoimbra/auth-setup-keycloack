package com.auth.setup;

import com.mashape.unirest.http.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpResponse;

@RequiredArgsConstructor
@Service
public class KeycloakRestService {

    @Value("${keycloak.token-uri}")
    private String keycloakTokenUri;

    @Value("${keycloak.user-info-uri}")
    private String keycloakUserInfo;

    @Value("${keycloak.logout}")
    private String keycloakLogout;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.authorization-grant-type}")
    private String grantType;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.scope}")
    private String scope;

    private final RestTemplate restTemplate;

    @SneakyThrows
    public String login(String username, String password) {
        Unirest.setTimeouts(0, 0);
        var response = Unirest.post(keycloakTokenUri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                 .field("grant_type", grantType)
                .field("password", password)
                .field("client_id", clientId)
                .field("client_secret", clientSecret)
                .field("scope", scope)
                .field("username", username)
                .asString();
        return response.getBody();
    }
}
