package com.qdot.tracing.shop.order;

import co.elastic.apm.api.ElasticApm;
import com.qdot.tracing.shop.cart.CartHandler;
import com.qdot.tracing.shop.cart.model.Cart;
import com.qdot.tracing.shop.order.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderHandler {

    private final StreamBridge streamBridge;
    private final CartHandler cartHandler;
    private final OrderRepository orderRepository;

    Mono<Order> create(ServerRequest serverRequest) {
        return cartHandler.get(serverRequest)
                .map(this::buildOrder)
                .map(orderRepository::save)
                .delayUntil(this::send)
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Order buildOrder(Cart cart) {
        return Order.builder()
                .status(Order.Status.IN_PROGRESS)
                .products(cart.getProducts())
                .build();
    }

    private Mono<Void> send(Order order) {
        return Mono.fromRunnable(() -> {
            ElasticApm.currentTransaction().addCustomContext("OrderId", order.getId().toString());
            log.info("sending order {}", order.getId());
            streamBridge.send("submitOrder-out-0", order);
        });
    }

}
