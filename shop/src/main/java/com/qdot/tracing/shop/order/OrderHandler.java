package com.qdot.tracing.shop.order;

import com.qdot.tracing.shop.cart.CartHandler;
import com.qdot.tracing.shop.cart.model.Cart;
import com.qdot.tracing.shop.order.model.Order;
import com.qdot.tracing.shop.product.ProductRepository;
import com.qdot.tracing.shop.product.model.Product;
import io.micrometer.context.ContextSnapshot;
import io.micrometer.observation.contextpropagation.ObservationThreadLocalAccessor;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderHandler {
    private final StreamBridge streamBridge;
    private final CartHandler cartHandler;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    Mono<Order> create(ServerRequest serverRequest) {
        return cartHandler.get(serverRequest)
                .delayUntil(this::saveProducts)
                .map(this::buildOrder)
                .flatMap(orderRepository::save)
                .delayUntil(this::send);
    }

    private Flux<Product> saveProducts(Cart cart) {
        return CollectionUtils.isEmpty(cart.getProducts())
                ? Flux.empty()
                : productRepository.saveAll(cart.getProducts());
    }

    private Order buildOrder(Cart cart) {
        return Order.builder()
                .id(UUID.randomUUID())
                .status(Order.Status.IN_PROGRESS)
                .products(cart.getProducts())
                .build();
    }

    private Mono<Boolean> send(Order order) {
        return Mono.deferContextual(contextView -> Mono.fromCallable(() -> {
            try(ContextSnapshot.Scope contextSnapshot = ContextSnapshot.setThreadLocalsFrom(contextView, ObservationThreadLocalAccessor.KEY)) {
                return streamBridge.send("orderCreate-out-0", order);
            }
        }));
    }

}
