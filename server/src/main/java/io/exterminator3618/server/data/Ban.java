package io.exterminator3618.server.data;

import io.exterminator3618.server.data.Account;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bans")
public class Ban {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @ManyToOne()
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private Account account;

    @Column(nullable = false)
    private LocalDateTime validFrom;

    @Column(nullable = true)
    private LocalDateTime validTo;

    @Column(nullable = true)
    private String reason;

    @PrePersist
    protected void onCreate() {
        validFrom = LocalDateTime.now();
    }

}
