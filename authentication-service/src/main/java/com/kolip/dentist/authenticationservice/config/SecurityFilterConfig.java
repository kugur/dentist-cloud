package com.kolip.dentist.authenticationservice.config;


import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@Slf4j
public class SecurityFilterConfig {

    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    public SecurityFilterConfig(
            @Qualifier("customSuccessHandler") AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.
                csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(sessionManagmentConfig -> sessionManagmentConfig.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        reqMatcherReq -> reqMatcherReq.requestMatchers("/api/user").permitAll())
                .authorizeHttpRequests(
                        reqMatcherReq -> reqMatcherReq.requestMatchers("/login/oauth2/code/google").permitAll())
                .authorizeHttpRequests(
                        reqMatcherReq -> reqMatcherReq.requestMatchers("/oauth2/authorization/google").permitAll())
                .authorizeHttpRequests(
                        authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry.anyRequest()
                                .authenticated());

        //Oauth2 Config
        http.
                oauth2Login(oauth2Config -> oauth2Config.defaultSuccessUrl("http://localhost:3000/")
                        .successHandler(authenticationSuccessHandler))
                .exceptionHandling(
                        exceptionHandlingConfigurer -> exceptionHandlingConfigurer.authenticationEntryPoint(
                                new Http403ForbiddenEntryPoint())).logout(logoutConfigurer -> {
                    HeaderWriterLogoutHandler clearSiteData = new HeaderWriterLogoutHandler(new LogoutClearSiteHeaderWriter());

                    //TODO(ugur) no need to redirect
                    logoutConfigurer.addLogoutHandler(clearSiteData);
                    logoutConfigurer.logoutSuccessHandler(((request, response, authentication) -> {
                        response.setStatus(HttpServletResponse.SC_OK);
                    }));
                });

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://accounts.google.com", "localhost:3000", "localhost:8181",
                                                      "https://localhost:3000", "https:// localhost:8181"));
        configuration.setAllowedMethods(Arrays.asList("OPTIONS", "GET", "POST", "DELETE", "PUT"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        configuration.setAllowCredentials(true);
        return source;
    }
}
