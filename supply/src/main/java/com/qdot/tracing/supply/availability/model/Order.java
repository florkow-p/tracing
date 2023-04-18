package com.qdot.tracing.supply.availability.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@Jacksonized
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

