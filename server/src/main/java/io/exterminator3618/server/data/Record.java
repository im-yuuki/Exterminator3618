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
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private GameMode gamemode;

    @Column(nullable = false)
    private LocalDateTime datetime;

    @Column(nullable = false)
    private Integer score;

    @Column(nullable = true)
    private Integer highestCombo;

    @Lob
    @Column(nullable = true)
    private byte[] data;

    @PrePersist
    protected void onCreate() {
        datetime = LocalDateTime.now();
    }

}
