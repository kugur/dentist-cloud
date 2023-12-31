package com.kolip.dentist.apigateway.config;

import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrors;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ugur.kolip on 30/11/2023.
 * Converter that is used to take Authorization token from cookie and convert.
 * At default, spring security try to find the token on request header.
 */
public class JwtTokenAuthenticationConverter implements ServerAuthenticationConverter {
    private String bearerTokenHeaderName = HttpHeaders.AUTHORIZATION;
    private static final Pattern authorizationPattern = Pattern.compile("^Bearer+(?<token>[a-zA-Z0-9-._~+/]+=*)$",
                                                                        Pattern.CASE_INSENSITIVE);

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.fromCallable(() -> token(exchange.getRequest())).map((token) -> {
            if (token.isEmpty()) {
                BearerTokenError error = invalidTokenError();
                throw new OAuth2AuthenticationException(error);
            }
            return new BearerTokenAuthenticationToken(token);
        });
    }

    private String token(ServerHttpRequest request) {
        String authorizationHeaderToken = resolveFromAuthorizationCookie(request.getCookies());

        if (authorizationHeaderToken != null) {
            return authorizationHeaderToken;
        }

        return null;
    }

    private String resolveFromAuthorizationCookie(MultiValueMap<String, HttpCookie> cookiesMap) {
        List<HttpCookie> authorizationCookieList = cookiesMap.get(bearerTokenHeaderName);
        if (authorizationCookieList == null || authorizationCookieList.isEmpty()) return null;

        if (authorizationCookieList.size() > 1) {
            BearerTokenError error = BearerTokenErrors.invalidRequest("Found multiple bearer tokens in the request");
            throw new OAuth2AuthenticationException(error);
        }

        String authorizationCookie = authorizationCookieList.get(0).getValue();

        if (!StringUtils.startsWithIgnoreCase(authorizationCookie, "bearer")) {
            return null;
        }
        Matcher matcher = authorizationPattern.matcher(authorizationCookie);
        if (!matcher.matches()) {
            BearerTokenError error = invalidTokenError();
            throw new OAuth2AuthenticationException(error);
        }
        return matcher.group("token").replace("+", "");
    }

    private static BearerTokenError invalidTokenError() {
        return BearerTokenErrors.invalidToken("Bearer token is malformed");
    }
}
