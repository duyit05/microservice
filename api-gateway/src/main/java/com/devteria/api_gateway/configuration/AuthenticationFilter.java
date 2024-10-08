package com.devteria.api_gateway.configuration;

import com.devteria.api_gateway.dto.response.ApiResponse;
import com.devteria.api_gateway.service.IdentityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered {

    IdentityService identityService;
    ObjectMapper objectMapper;

    @NonFinal
    private final String[] PUBLIC_ENDPOINTS = {
            "/identity/auth/log-in",
            "/identity/users/registration"

    };

    @Value("${app.api-prefix}")
    @NonFinal
    private String API_PREFIX;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Enter authentication filter ....");

        if (isPublicEndpoints(exchange.getRequest())) {
            log.info("Public endpoint matched, skipping authentication");
            return chain.filter(exchange);
        } else {
            log.info("Not a public endpoint, proceeding with authentication");
        }


        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);

        if (CollectionUtils.isEmpty(authHeader)) {
            return unauthenticated(exchange.getResponse());
        }


        String token = authHeader.get(0).replace("Bearer ", "").trim();
        log.info("Extracted Token: {}", token);

        if (!token.contains(".")) {
            log.error("Invalid Token format");
            return unauthenticated(exchange.getResponse());
        }

        // VERIFY TOKEN USING IDENTITY SERVICE
        return identityService.introspect(token)
                .flatMap(introspectResponseApiResponse -> {
                    if (introspectResponseApiResponse.getResult().isValid()) {
                        log.info("Token is valid");
                        return chain.filter(exchange); // Token valid, proceed
                    } else {
                        log.warn("Token is invalid");
                        return unauthenticated(exchange.getResponse()); // Token invalid, return 401
                    }
                })
                .doOnError(error -> {
                    log.error("Error during token introspection", error);
                });
    }

    @Override
    public int getOrder() {

        return -1;
    }

    public Mono<Void> unauthenticated(ServerHttpResponse response) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(1401)
                .message("Unauthenticated")
                .build();

        String body = null;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }

    private boolean isPublicEndpoints(ServerHttpRequest request) {
        return Arrays.stream(PUBLIC_ENDPOINTS).anyMatch(s -> request.getURI().getPath().matches(API_PREFIX + s));
    }

}
