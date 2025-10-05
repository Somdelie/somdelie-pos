package com.somdelie_pos.somdelie_pos.modal;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private Integer quantity;

    private Double price;

    private Double sellPrice;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Order order;

}
