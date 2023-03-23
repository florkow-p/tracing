package com.qdot.tracing.shop.order.model;

import com.qdot.tracing.shop.product.model.Product;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.UUID;

@Table("public.order")
@Value
@Jacksonized
@Builder
@RequiredArgsConstructor
public class Order {

    UUID id;

    @Transient
    List<Product> products;

    Status status;

    public enum Status {

        IN_PROGRESS("inProgress"),
        COMPLETED("completed"),
        CANCELED("canceled");

        String value;

        Status(String value) {
            this.value = value;
        }

    }

}
