package com.qdot.tracing.shop.order;

import com.qdot.tracing.shop.order.model.Order;
import io.micrometer.observation.annotation.Observed;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class OrderRouterFunction {

    @Bean
    @Observed(name = "order")
    RouterFunction<ServerResponse> handleOrder(final OrderHandler orderHandler) {
        return route(POST("/order/{cartId}"), createOrder(orderHandler));
    }

    HandlerFunction<ServerResponse> createOrder(OrderHandler orderHandler) {
        return request -> ServerResponse.ok().body(orderHandler.create(request), Order.class);
    }

}
