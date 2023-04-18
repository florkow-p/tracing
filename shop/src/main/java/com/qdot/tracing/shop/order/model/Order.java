package com.qdot.tracing.shop.order.model;

import com.qdot.tracing.shop.product.model.Product;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import java.util.List;
import java.util.UUID;

@Table(name = "orders")
@Entity
@Value
@Jacksonized
@Builder
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    List<Product> products;

    @With
    @Enumerated(EnumType.STRING)
    Status status;

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
