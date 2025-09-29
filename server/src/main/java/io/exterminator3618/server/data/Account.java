package io.exterminator3618.server.data;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "accounts", indexes = @Index(name = "idx_username", columnList = "username", unique = true))
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 32)
    private String username;

    @Column(nullable = false, length = 64)
    private String passwordHash;

    @Column(nullable = false)
    private LocalDateTime createdAt;

}
