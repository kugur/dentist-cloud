package com.kolip.dentist.authenticationservice.config;


import com.kolip.dentist.authenticationservice.service.CustomTokenService;
import com.kolip.dentist.authenticationservice.service.CustomUserService;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class SecurityConfig {

    @Value("${jwt.public.key}")
    RSAPublicKey key;

    @Value("${jwt.private.key}")
    RSAPrivateKey priv;

    @Autowired
    CustomUserService customUserService;

    private JwtEncoder encoder;
    @Autowired
    public void setJwtEncoder(JwtEncoder encoder) {
        this.encoder = encoder;
    };
    public String createToken(Authentication authentication) {
        Instant now = Instant.now();
        long expiry = 36000L;
        // @formatter:off
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();
        // @formatter:on
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(AbstractHttpConfigurer::disable).
                csrf(AbstractHttpConfigurer::disable)
//                .httpBasic(Customizer.withDefaults()).sessionManagement(
//                        sessionManagmentConfig -> sessionManagmentConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(reqMatcherReq -> reqMatcherReq.requestMatchers(HttpMethod.POST, "/api/user").permitAll())
                .authorizeHttpRequests(reqMatcherReq -> reqMatcherReq.requestMatchers( "/login/oauth2/code/google").permitAll())
                .authorizeHttpRequests(reqMatcherReq -> reqMatcherReq.requestMatchers( "/oauth2/authorization/google").permitAll())
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry.anyRequest().authenticated());

        //Oauth2 Config
        http.oauth2Login(oauth2Config -> oauth2Config.defaultSuccessUrl("http://localhost:3000/")
                .successHandler((request, response, authentication) -> {
                    log.info("on success handler on oauth2Login :: " + authentication.toString());
                    String email = String.valueOf(((OidcUser)authentication.getPrincipal()).getClaims().get("email"));
                    response.setHeader("token", email);
                    Cookie tokenCookie = new Cookie("Authorization", URLEncoder.encode("Bearer " + createToken(authentication), StandardCharsets.UTF_8));
                    tokenCookie.setHttpOnly(true);
                    tokenCookie.setPath("/");

                    response.addCookie(tokenCookie);
                    
//                    RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
//                    redirectStrategy.sendRedirect(request, response, "http://localhost:3000/");
                })).exceptionHandling(
                exceptionHandlingConfigurer -> exceptionHandlingConfigurer.authenticationEntryPoint(
                        new Http403ForbiddenEntryPoint())).logout(logoutConfigurer -> {
            logoutConfigurer.logoutSuccessHandler(((request, response, authentication) -> {
                //TODO(ugur) no need to redirect
                response.setStatus(HttpServletResponse.SC_OK);
            }));
        });


        return http.build();
    }

//    @Bean
//    UserDetailsService users() {
//        // @formatter:off
//            return new InMemoryUserDetailsManager(
//                    User.withUsername("user")
//                            .password("{noop}password")
//                            .authorities("app")
//                            .build()
//            );
//            // @formatter:on
//    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.key).privateKey(this.priv).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }
}
