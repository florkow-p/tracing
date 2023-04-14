package com.qdot.tracing.shop.cart;

import com.qdot.tracing.shop.cart.exception.CartNotFoundException;
import com.qdot.tracing.shop.cart.model.Cart;
import com.qdot.tracing.shop.product.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartHandler {

    private static final String ID_PATH = "cartId";

    @Value("${redis.ttl}")
    private Duration ttl;

    private final ReactiveRedisOperations<String, Cart> redisOperations;

    public Mono<Cart> get(final ServerRequest serverRequest) {
        final String id = serverRequest.pathVariable(ID_PATH);

        return redisOperations.opsForValue().get(id)
                .doOnNext(cart -> log.info("found cart for id {}", id))
                .switchIfEmpty(Mono.error(new CartNotFoundException("Cart Not Found")))
                .doOnError(throwable -> log.error("Cart not found for id {}", id));
    }

    public Mono<Cart> create() {
        return Mono.just(Cart.builder().id(UUID.randomUUID().toString()).build())
                .delayUntil(cart -> redisOperations.opsForValue().set(cart.getId(), cart, ttl));
    }

    public Mono<Cart> addProduct(final ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Product.class)
                .flatMap(product -> get(serverRequest)
                        .map(cart -> {
                            cart.addProduct(product);
                            return cart;
                        })
                ).delayUntil(cart -> redisOperations.opsForValue().set(cart.getId(), cart, ttl));
    }

}
