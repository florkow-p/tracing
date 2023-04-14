package com.qdot.tracing.shop.cart;

import com.qdot.tracing.shop.cart.model.Cart;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class CartRouterFunction {

    @Bean
    RouterFunction<ServerResponse> handleCart(final CartHandler cartHandler) {
        return route(GET("/cart/{cartId}"), getCart(cartHandler))
                .and(route(POST("/cart"), createCart(cartHandler)))
                .and(route(POST("/cart/{cartId}"), addProduct(cartHandler)));
    }

    HandlerFunction<ServerResponse> getCart(CartHandler cartHandler) {
        return request -> ServerResponse.ok().body(cartHandler.get(request), Cart.class);
    }

    HandlerFunction<ServerResponse> createCart(CartHandler cartHandler) {
        return request -> ServerResponse.ok().body(cartHandler.create(), Cart.class);
    }

    HandlerFunction<ServerResponse> addProduct(CartHandler cartHandler) {
        return request -> ServerResponse.ok().body(cartHandler.addProduct(request), Cart.class);
    }

}
