package com.kolip.dentist.apigateway.config;

import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Created by ugur.kolip on 30/11/2023.
 * Let to add multiple url to match for using on security filter.
 */
public class MultiPathPatternWebExchangeMatcher implements ServerWebExchangeMatcher {
    private final List<String> patternList;

    public MultiPathPatternWebExchangeMatcher(String... pattern) {
        this.patternList = List.of(pattern);
    }

    @Override
    public Mono<MatchResult> matches(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        PathContainer path = request.getPath().pathWithinApplication();

        boolean match = this.patternList.stream().anyMatch(pattern -> pattern.equals(path.value()));
        if (!match) {
            return MatchResult.notMatch();
        }
        return MatchResult.match();
    }
}
