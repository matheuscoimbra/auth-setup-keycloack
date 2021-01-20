package com.auth.setup;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import com.nimbusds.jwt.JWTParser;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtBearerTokenAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.stereotype.Component;

@Component
public class TenantAuthenticationManagerResolver implements AuthenticationManagerResolver<HttpServletRequest> {
    private final BearerTokenResolver resolver = new DefaultBearerTokenResolver();
    private final Map<String, String> tenants = new HashMap<>();

    private final Map<String, AuthenticationManager> authenticationManagers = new HashMap<>();

    public TenantAuthenticationManagerResolver() {
        this.tenants.put("coimbra", "http://localhost:8180/auth/realms/flow");
        this.tenants.put("coimbra2", "http://localhost:8180/auth/realms/teste");
    }

    @Override
    public AuthenticationManager resolve(HttpServletRequest request) {
        return this.authenticationManagers.computeIfAbsent(toTenant(request), this::fromTenant);
    }

    private String toTenant(HttpServletRequest request) {
        try {
            String token = this.resolver.resolve(request);
            return (String) JWTParser.parse(token).getJWTClaimsSet().getClaim("family_name");
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private AuthenticationManager fromTenant(String tenant) {
        return Optional.ofNullable(this.tenants.get(tenant))
                .map(JwtDecoders::fromIssuerLocation)
                .map(JwtAuthenticationProvider::new)
                .orElseThrow(() -> new IllegalArgumentException("unknown tenant"))::authenticate;
    }

    //@KafkaListener(topics="tenants")
    public void action(Map<String, Map<String, Object>> action) {
        if (action.containsKey("created")) {
            Map<String, Object> tenant = action.get("created");
            String alias = (String) tenant.get("alias");
            String issuerUri = (String) tenant.get("issuerUri");
            this.tenants.put(alias, issuerUri);
        }
    }
}