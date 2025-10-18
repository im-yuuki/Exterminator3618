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
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account", referencedColumnName = "id", nullable = false)
    private Account account;

    @Column(name = "validFrom", nullable = false)
    private LocalDateTime validFrom;

    @Column(name = "validTo", nullable = true)
    private LocalDateTime validTo;

    @Column(name = "reason", nullable = true)
    private String reason;
}
