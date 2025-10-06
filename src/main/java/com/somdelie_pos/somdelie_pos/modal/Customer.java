package com.somdelie_pos.somdelie_pos.modal;

import jakarta.persistence.Column;

import org.hibernate.annotations.GenericGenerator;import jakarta.persistence.Entity;

import org.hibernate.annotations.GenericGenerator;import jakarta.persistence.GeneratedValue;

import org.hibernate.annotations.GenericGenerator;import jakarta.persistence.Id;

import org.hibernate.annotations.GenericGenerator;import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false)
    private String fullName;

    private String email;

    private String phone;

    private String address;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}

