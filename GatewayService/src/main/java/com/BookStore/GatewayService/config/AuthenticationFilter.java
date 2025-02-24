package com.BookStore.GatewayService.config;


import com.BookStore.GatewayService.Service.LoginService;
import com.BookStore.GatewayService.dto.LoginRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import org.springframework.web.server.ServerWebExchange;



import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
    private LoginService loginService;

    @NonFinal
    private String[] publicEndpoints = {
            "authentication-service/login",
            "authentication-service/login-google",
            "authentication-service/login-google-callback",

//            "/oauth2/**",
            "authentication-service/valid",
            "authentication-service/dang-ky",
            "authentication-service/quen-mat-khau",
            "quan-ly-sach-service/hinh-anh/get",
            "quan-ly-sach-service/hinh-anh/getUser",
            "sach-service/hinh-anh/get",
            "sach-service/hinh-anh/getUser",
            "sach-service/sach/ds-sach",
            "sach-service/sach/chi-tiet-sach",
            "sach-service/sach/tim-sach",
            "sach-service/sach/tong-sl-sach-tim",
            "sach-service/sach/ds-sach-ban-chay",
            "sach-service/binh-luan/ds-binh-luan",
            "sach-service/binh-luan/so-luong-binh-luan",
            "sach-service/sach/sach-tuong-tu",
            "sach-service/sach/search",
            "sach-service/sach/search-amount",
            "sach-service/sach/ds-tg",
            "sach-service/sach/ds-tl",

            // test
            "order-service/.*"

    };

    private String apiPrefix = "/api/";

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        System.out.println("check filter api gateway");
        // Bỏ qua những api public
        if (isPublicEndpoint(exchange.getRequest()))
            return chain.filter(exchange);

        // lấy token từ header
        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (CollectionUtils.isEmpty(authHeader)) {
            logger.error("Authorization header is empty");
            return unauthenticated(exchange.getResponse());
        }
        String token = authHeader.get(0).replace("Bearer ", "");

        // call service thông qua webclient để xác thực token và trả kết quả
        return loginService.validToken(new LoginRequest(token)).flatMap(result -> {
            if (result.isValid()) {
                logger.info("Login successful");
                return chain.filter(exchange);
            } else {
                return unauthenticated(exchange.getResponse());
            }
        }).onErrorResume(throwable -> unauthenticated(exchange.getResponse()));
    }

    private boolean isPublicEndpoint(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        logger.info("request path: "+path);
        return Arrays.stream(publicEndpoints)
                .anyMatch(s -> path.matches(apiPrefix + s))
                || path.matches("/actuator/health")
                || path.matches("/oauth2/.*");
    }

    Mono<Void> unauthenticated(ServerHttpResponse response) {
        String body = "Unauthenticated";
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }

}
