/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kolip.dentist.apigateway.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.access.server.BearerTokenServerAccessDeniedHandler;
import org.springframework.security.oauth2.server.resource.web.server.BearerTokenServerAuthenticationEntryPoint;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

/**
 * Security configuration for the main application.
 *
 * @author Josh Cummings
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${jwt.public.key}")
    RSAPublicKey key;

    @Value("${jwt.private.key}")
    RSAPrivateKey priv;

    @Bean
    ReactiveJwtDecoder jwtDecoder() {
        return NimbusReactiveJwtDecoder.withPublicKey(this.key).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.key).privateKey(this.priv).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    /**
     * Created Security filter for public endpoints to prevent authentication for request with Authentication header.
     *
     * @param http
     * @return
     */
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    SecurityWebFilterChain publicHttpSecurityFilterChain(ServerHttpSecurity http) {

        http.securityMatcher(
                new MultiPathPatternWebExchangeMatcher("/login/oauth2/code/google", "/oauth2/authorization/google",
                                                       "/api/token", "/api/user", "/api/login"));
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(corsSpec -> corsSpec.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(ServerWebExchange request) {
                        CorsConfiguration corsConfiguration = new CorsConfiguration();
                        corsConfiguration.setAllowedOrigins(
                                List.of("https://accounts.google.com", "localhost:3000", "http://localhost:3000",
                                        "localhost:8181", "http://localhost:8181", "https://localhost:3000"));
                        corsConfiguration.setAllowedMethods(List.of("*"));
                        corsConfiguration.setAllowedHeaders(List.of("*"));
                        corsConfiguration.setAllowCredentials(true);
                        return corsConfiguration;
                    }
                }));
        http.authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec.pathMatchers("/api/token").permitAll())
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec.pathMatchers("api/token").permitAll())
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec.pathMatchers("api/login").permitAll())
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec.pathMatchers("logout").permitAll())
                .authorizeExchange(
                        authorizeExchangeSpec -> authorizeExchangeSpec.pathMatchers(HttpMethod.POST, "/api/user")
                                .permitAll())
                .authorizeExchange(
                        authorizeExchangeSpec -> authorizeExchangeSpec.pathMatchers("/login/oauth2/code/google")
                                .permitAll())
                .authorizeExchange(
                        authorizeExchangeSpec -> authorizeExchangeSpec.pathMatchers("/oauth2/authorization/google")
                                .permitAll());
        return http.build();
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(corsSpec -> corsSpec.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(ServerWebExchange request) {
                        CorsConfiguration corsConfiguration = new CorsConfiguration();
                        corsConfiguration.setAllowedOrigins(
                                List.of("https://accounts.google.com", "localhost:3000", "http://localhost:3000",
                                        "localhost:8181", "http://localhost:8181", "https://localhost:3000"));
                        corsConfiguration.setAllowedMethods(List.of("*"));
                        corsConfiguration.setAllowedHeaders(List.of("*"));
                        corsConfiguration.setAllowCredentials(true);
                        return corsConfiguration;
                    }
                }))
                .authorizeExchange(exchanges -> exchanges.anyExchange().authenticated())
                .oauth2ResourceServer(
                        oauth2ResConf -> oauth2ResConf.bearerTokenConverter(new JwtTokenAuthenticationConverter())
                                .jwt(Customizer.withDefaults())).exceptionHandling(
                        (exceptions) -> exceptions.authenticationEntryPoint(new BearerTokenServerAuthenticationEntryPoint())
                                .accessDeniedHandler(new BearerTokenServerAccessDeniedHandler()));
        return http.build();
    }

}