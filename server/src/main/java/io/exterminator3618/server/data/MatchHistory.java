package io.exterminator3618.server.data;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "matchhistory")
public class MatchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(name = "datetime", nullable = false)
    private LocalDateTime datetime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record1", referencedColumnName = "id", nullable = false)
    private Record record1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record2", referencedColumnName = "id", nullable = false)
    private Record record2;
}
