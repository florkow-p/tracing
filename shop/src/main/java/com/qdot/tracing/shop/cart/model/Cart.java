package com.qdot.tracing.shop.cart.model;

import com.qdot.tracing.shop.product.model.Product;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.LinkedList;
import java.util.List;

@Value
@Builder
@Jacksonized
public class Cart {

    String id;

    @Builder.Default
    List<Product> products = new LinkedList<>();

    public void addProduct(final Product product) {
        this.products.add(product);
    }

}
