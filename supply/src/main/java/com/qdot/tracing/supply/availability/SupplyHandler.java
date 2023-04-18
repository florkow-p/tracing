package com.qdot.tracing.supply.availability;

import com.qdot.tracing.supply.availability.model.Order;
import com.qdot.tracing.supply.availability.model.OrderAvailability;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@Service
public class SupplyHandler {

    private final static String UNAVAILABLE_PRODUCT = "test";

    public Mono<OrderAvailability> isAvailable(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Order.class)
                .flatMapIterable(Order::getProducts)
                .map(product -> !UNAVAILABLE_PRODUCT.equals(product.getName()))
                .reduce((aBoolean, aBoolean2) -> aBoolean && aBoolean2)
                .map(aBoolean -> OrderAvailability.builder().isAvailable(aBoolean).build());
    }

}
