package com.qdot.tracing.order.supply;

import com.qdot.tracing.order.submit.model.Order;
import com.qdot.tracing.order.supply.model.OrderAvailability;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SupplyClient {

    private final WebClient webClient;

    public Mono<OrderAvailability> getOrderAvailability(Order order) {
        return webClient.post()
                .uri("http://172.17.0.1:8082/order/availability")
                .bodyValue(order)
                .retrieve()
                .bodyToMono(OrderAvailability.class);
    }

}
