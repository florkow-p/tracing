package com.qdot.tracing.shop.cart.model;

import com.qdot.tracing.shop.product.model.Product;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Jacksonized
@Builder
public class Cart {

    String id;

    List<Product> products;

    public void addProduct(final Product product) {
        this.products.add(product);
    }

}
