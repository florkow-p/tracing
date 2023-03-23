package com.qdot.tracing.shop.order;

import org.springframework.messaging.MessageChannel;

public interface CreateOrderSource {

    MessageChannel createOrder();

}
