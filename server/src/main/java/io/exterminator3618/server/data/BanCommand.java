package io.exterminator3618.server.data;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ban_commands", indexes = @Index(name = "idx_account_id", columnList = "account_id"))
public class BanCommand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false)
    private LocalDateTime validFrom;

    @Column
    private LocalDateTime validTo;

    @Column(length = 256)
    private String reason;

}
