package com.qdot.tracing.order.submit.model;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Getter
@Builder
@Jacksonized
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Product {

    private final UUID id;

    private final String name;

}

