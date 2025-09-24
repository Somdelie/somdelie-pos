package com.somdelie_pos.somdelie_pos.modal;

import com.somdelie_pos.somdelie_pos.domain.StoreStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Store {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String brandName;

    @OneToOne
    private User storeAdmin;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private String description;

    private String storeType;

    private StoreStatus status;

    @Embedded
    private StoreContact contact = new StoreContact();

    @PrePersist
    protected void onCreate(){
        createdDate = LocalDateTime.now();
        status = StoreStatus.PENDING;
    }

    @PreUpdate
    protected void onUpdate(){
        updatedDate = LocalDateTime.now();
    }
}
