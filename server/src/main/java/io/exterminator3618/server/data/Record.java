package io.exterminator3618.server.data;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "records")
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    // bỏ unique = true để 1 account có nhiều record phục vụ cho matchHistory
    @JoinColumn(name = "account", referencedColumnName ="id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gamemode", referencedColumnName = "id",nullable = false)
    private GameMode gamemode;

    @Column(name = "datetime", nullable = false)
    private LocalDateTime datetime;

    @Column(name = "score", nullable = false)
    private Integer score;

    @Column(name = "highestCombo", nullable = true)
    private Integer highestCombo;

    @Lob
    @Column(name = "data", nullable = true)
    private byte[] data;

}
