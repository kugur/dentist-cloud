package com.kolip.dentist.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import reactor.core.publisher.Hooks;

/**
 * Created by ugur.kolip on 30/11/2023.
 *
 * Starting point for Api gate-way.
 * Using for routing and security by using jwt token.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {
    public static void main(String[] args) {
        Hooks.enableAutomaticContextPropagation();
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
