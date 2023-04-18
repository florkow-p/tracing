package com.qdot.tracing.supply.availability;

import com.qdot.tracing.supply.availability.model.OrderAvailability;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SupplyRouterFunction {

    @Bean
    RouterFunction<ServerResponse> handleSupply(SupplyHandler supplyHandler) {
        return route(POST("/order/availability"), isAvailable(supplyHandler));
    }

    HandlerFunction<ServerResponse> isAvailable(SupplyHandler supplyHandler) {
        return request -> ServerResponse.ok().body(supplyHandler.isAvailable(request), OrderAvailability.class);
    }

}
