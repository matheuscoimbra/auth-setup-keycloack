package com.auth.setup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import javax.servlet.http.HttpServletRequest;

@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver;
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());

       /* JwtIssuerAuthenticationManagerResolver authenticationManagerResolver = new JwtIssuerAuthenticationManagerResolver
                ("http://localhost:8180/auth/realms/flow");*/

        //http://localhost:8180/auth/realms/teste/.well-known/openid-configuration

        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/albums")
                .hasAuthority("ROLE_developer2")
                .anyRequest()
                .authenticated()
                .and()
                .oauth2ResourceServer(o -> o.authenticationManagerResolver(this.authenticationManagerResolver))
               ;
                //.oauth2ResourceServer(o -> o.authenticationManagerResolver(this.authenticationManagerResolver))

                 // apply jwt converter;

       // http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


    private static final String[] PUBLIC_MATCHERS = {
            "/auth/login"
    };



    @Override
    public void configure(org.springframework.security.config.annotation.web.builders.WebSecurity web) throws Exception {
        web.ignoring().antMatchers(PUBLIC_MATCHERS);
    }


//	@Bean
//	CorsConfigurationSource corsConfigurationSource() {
//		CorsConfiguration corsConfiguration = new CorsConfiguration();
//		corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
//		corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST"));
//		corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
//
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", corsConfiguration);
//
//		return source;
//	}
//
}