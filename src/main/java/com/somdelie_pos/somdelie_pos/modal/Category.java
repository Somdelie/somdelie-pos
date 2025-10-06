package com.somdelie_pos.somdelie_pos.modal;

import jakarta.persistence.*;

import org.hibernate.annotations.GenericGenerator;import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private Store store;
}

