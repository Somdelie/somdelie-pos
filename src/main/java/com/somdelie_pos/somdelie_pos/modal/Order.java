package com.somdelie_pos.somdelie_pos.modal;

import com.somdelie_pos.somdelie_pos.domain.OrderStatus;
import com.somdelie_pos.somdelie_pos.domain.PaymentType;
import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    private Double totalAmount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    private Branch branch;

    @ManyToOne
    private User cashier;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    private PaymentType paymentType;

    @Enumerated(EnumType.STRING) // ✅ Add this
    private OrderStatus status; // ✅ Add this

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = OrderStatus.COMPLETED; // ✅ Default to COMPLETED for POS
        }
    }
}
