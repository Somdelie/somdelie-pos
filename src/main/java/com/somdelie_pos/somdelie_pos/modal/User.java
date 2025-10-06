package com.somdelie_pos.somdelie_pos.modal;


import com.somdelie_pos.somdelie_pos.domain.UserRole;
import jakarta.persistence.Column;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import org.hibernate.annotations.GenericGenerator;import jakarta.persistence.Id;

import org.hibernate.annotations.GenericGenerator;import jakarta.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id") // Only use id for equals/hashCode
public class User {


    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    @Email(message = "Please enter a valid email address")
    private String email;

    @ManyToOne
    private Store store;

    @ManyToOne
    private Branch branch;

    // phone
    private String phone;

    // role
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private String password;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private LocalDateTime lastLogin;
}



