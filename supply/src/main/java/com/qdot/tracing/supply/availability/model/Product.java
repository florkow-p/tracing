package com.qdot.tracing.supply.availability.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Getter
@Builder
@Jacksonized
@RequiredArgsConstructor
public class Product {

    private final UUID id;

    private final String name;

}

