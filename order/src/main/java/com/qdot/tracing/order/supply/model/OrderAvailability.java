package com.qdot.tracing.order.supply.model;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class OrderAvailability {

    private final Boolean isAvailable;

}
