package com.somdelie_pos.somdelie_pos.modal;

import jakarta.persistence.*;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @Column(updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    private Integer quantity;

    private Double price;

    private Double sellPrice;

    @ManyToOne
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(name = "fk_order_item_order"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;

}
