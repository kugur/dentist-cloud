package com.kolip.dentist.apigateway.config;

import org.springframework.http.HttpMethod;
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
    private final List<Pair> patternList;

    public MultiPathPatternWebExchangeMatcher(List<Pair> patternList) {
        this.patternList = patternList;
    }


    @Override
    public Mono<MatchResult> matches(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        PathContainer path = request.getPath().pathWithinApplication();

        boolean match = this.patternList.stream()
                .anyMatch(pattern -> pattern.path.equals(path.value()) &&
                        (pattern.method == null || pattern.method == request.getMethod()));
        if (!match) {
            return MatchResult.notMatch();
        }
        return MatchResult.match();
    }

    public static class Pair {
        public HttpMethod method;
        public String path;

        public Pair(HttpMethod method, String path) {
            this.method = method;
            this.path = path;
        }
        public Pair(String path) {
            this.path = path;
        }

    }
}
