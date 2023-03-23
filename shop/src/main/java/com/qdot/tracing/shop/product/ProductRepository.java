package com.qdot.tracing.shop.product;

import com.qdot.tracing.shop.product.model.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface ProductRepository extends R2dbcRepository<Product, UUID> {

    @Query("SELECT * FROM public.product WHERE id = :id")
    Flux<Product> findByOrderId(String id);

}
