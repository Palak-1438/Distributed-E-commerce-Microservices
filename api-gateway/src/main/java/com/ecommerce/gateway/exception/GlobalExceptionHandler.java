package com.ecommerce.gateway.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@Order(-1)
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("Gateway Error: {}", ex.getMessage(), ex);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (ex instanceof ResponseStatusException) {
            status = (HttpStatus) ((ResponseStatusException) ex).getStatusCode();
        }

        Map<String, Object> errorAttributes = new HashMap<>();
        errorAttributes.put("timestamp", LocalDateTime.now().toString());
        errorAttributes.put("path", exchange.getRequest().getPath().value());
        errorAttributes.put("status", status.value());
        errorAttributes.put("error", status.getReasonPhrase());
        errorAttributes.put("message", ex.getMessage());

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(errorAttributes);
            DataBuffer buffer = bufferFactory.wrap(bytes);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            log.error("Error writing response", e);
            return exchange.getResponse().setComplete();
        }
    }
}
