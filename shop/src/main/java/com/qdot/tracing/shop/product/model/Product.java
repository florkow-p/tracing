package com.qdot.tracing.shop.product.model;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Jacksonized
@Builder
public class Product {

    UUID id;

    String name;

}
