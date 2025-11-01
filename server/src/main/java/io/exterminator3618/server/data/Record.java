package io.exterminator3618.server.data;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "records")
@Data
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private Account account;

    @Column(nullable = false)
    private LocalDateTime datetime;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = true)
    private Match match;

    @Column(nullable = false)
    private Integer score;

    @Column(nullable = true)
    private Integer highestCombo;

    @Lob
    @Column
    private String data;

    @PrePersist
    protected void onCreate() {
        datetime = LocalDateTime.now();
    }

}
