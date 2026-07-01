package com.ecommerce.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/userServiceFallback")
    public Mono<ResponseEntity<Map<String, String>>> userServiceFallback() {
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("message", "User Service is currently unavailable. Please try again later.")));
    }

    @GetMapping("/productServiceFallback")
    public Mono<ResponseEntity<Map<String, String>>> productServiceFallback() {
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("message", "Product Service is currently unavailable. Please try again later.")));
    }
}
