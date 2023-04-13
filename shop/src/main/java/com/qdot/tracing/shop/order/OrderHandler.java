package com.qdot.tracing.shop.order;

import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.Scope;
import co.elastic.apm.api.Span;
import com.qdot.tracing.shop.cart.CartHandler;
import com.qdot.tracing.shop.cart.model.Cart;
import com.qdot.tracing.shop.order.model.Order;
import com.qdot.tracing.shop.utils.ContextUtils;
import io.micrometer.context.ContextSnapshot;
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
                .flatMap(order -> Mono.defer(() -> Mono.fromCallable(() -> orderRepository.save(order))))
                .subscribeOn(Schedulers.boundedElastic())
                .delayUntil(this::send);
    }

    private Order buildOrder(Cart cart) {
        return Order.builder()
                .status(Order.Status.IN_PROGRESS)
                .products(cart.getProducts())
                .build();
    }

    private Mono<Void> send(Order order) {
        return Mono.deferContextual(contextView -> {
            Span span = ElasticApm.currentSpan().startSpan("messaging", "rabbitmq", "send");

            return Mono.fromRunnable(() -> {
                try (ContextSnapshot.Scope contextSnapshot = ContextUtils.getContextSnapshot(contextView)) {
                    try (Scope scope = span.activate()) {
                        span.setName("SEND order event");
                        span.setServiceTarget("rabbitmq", null);
                        span.setLabel("myLabel", "value");
                    }

                    log.info("sending order {}", order.getId());
                    streamBridge.send("orderCreate-out-0", order);

                    span.end();
                } catch (Exception e) {
                    span.captureException(e);
                }
            });
        });
    }

}
