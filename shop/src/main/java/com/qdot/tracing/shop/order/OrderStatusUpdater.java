package com.qdot.tracing.shop.order;

import com.qdot.tracing.shop.order.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Slf4j
@Configuration
public class OrderStatusUpdater {

    @Bean
    Consumer<Message<Order>> updateOrderStatus(OrderRepository orderRepository) {
        return orderMessage -> {
            Order order = orderMessage.getPayload();
            log.info("update order status [{}, {}]", order.getId(), order.getStatus());
            orderRepository.save(order);
        };
    }

}
