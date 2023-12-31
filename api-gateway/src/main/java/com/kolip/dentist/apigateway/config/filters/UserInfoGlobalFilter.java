package com.kolip.dentist.apigateway.config.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

/**
 * Created GlobalFilter that adds userInfo header to request.
 */
@Slf4j
public class UserInfoGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> securityContext.getAuthentication())
                .filter(authentication -> authentication instanceof JwtAuthenticationToken)
                .map(authentication -> (JwtAuthenticationToken) authentication)
                .doOnNext(jwtAuthenticationToken -> {

                    // Add a custom header to the exchange
                    exchange.getRequest().mutate()
                            .header("userInfo", jwtAuthenticationToken.getName())
                            .header("roles", jwtAuthenticationToken.getAuthorities()
                                    .stream()
                                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                                    .collect(Collectors.toList()).toString())
                            .build();

                })
                .then(chain.filter(exchange));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
