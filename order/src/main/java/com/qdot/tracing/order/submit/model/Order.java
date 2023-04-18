package com.qdot.tracing.order.submit.model;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@Jacksonized
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Order {

    private final UUID id;

    private final List<Product> products;

    @With
    private final Status status;

    public enum Status {

        IN_PROGRESS("inProgress"),
        COMPLETED("completed"),
        CANCELED("canceled");

        final String value;

        Status(String value) {
            this.value = value;
        }

    }

}

