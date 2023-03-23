package com.qdot.tracing.shop.cart;

import com.qdot.tracing.shop.cart.model.Cart;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CartRouterFunction {

    @Bean
    @Observed(name = "cart")
    RouterFunction<ServerResponse> handleCart(final CartHandler cartHandler) {
        return route(GET("/cart/{cartId}"), request -> ServerResponse.ok()
                .body(cartHandler.get(request), Cart.class))
                .and(route(POST("/cart"), request -> ServerResponse.ok()
                        .body(cartHandler.create(request), Cart.class)))
                .and(route(POST("/cart/{cartId}"), request -> ServerResponse.ok()
                        .body(cartHandler.addProduct(request), Cart.class)));
    }

}
