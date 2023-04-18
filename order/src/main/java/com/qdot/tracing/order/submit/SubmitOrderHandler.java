package com.qdot.tracing.order.submit;

import com.qdot.tracing.order.submit.model.Order;
import com.qdot.tracing.order.supply.SupplyClient;
import com.qdot.tracing.order.supply.model.OrderAvailability;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubmitOrderHandler {

    private final SupplyClient supplyClient;

    @Bean
    Consumer<Message<Order>> submitOrder(StreamBridge streamBridge) {
        return orderMessage -> {
            Mono.just(orderMessage.getPayload())
                    .doOnNext(order -> log.info("received order {}", order))
                    .flatMap(order -> supplyClient.getOrderAvailability(orderMessage.getPayload())
                            .filter(OrderAvailability::getIsAvailable)
                            .map(orderAvailability -> updateOrder(streamBridge, order.withStatus(Order.Status.COMPLETED)))
                            .switchIfEmpty(Mono.defer(() ->
                                    Mono.fromRunnable(() -> {
                                        updateOrder(streamBridge, order.withStatus(Order.Status.CANCELED));
                                    }))
                            )
                    ).subscribe();
        };
    }

    boolean updateOrder(StreamBridge streamBridge, Order order) {
        return streamBridge.send("updateOrderStatus-out-0", buildMessage(order));
    }

    Message<Order> buildMessage(Order order) {
        return MessageBuilder
                .withPayload(order)
                .build();
    }

}
