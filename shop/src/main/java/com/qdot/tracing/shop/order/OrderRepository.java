package com.qdot.tracing.shop.order;

import com.qdot.tracing.shop.order.model.Order;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface OrderRepository extends R2dbcRepository<Order, UUID> {
}
