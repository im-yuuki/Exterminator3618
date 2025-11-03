package io.exterminator3618.server.data;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
@Data
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime datetime;

    @Column
    private String mode;

    @Column
    private String mapCode;

    @PrePersist
    protected void onCreate() {
        datetime = LocalDateTime.now();
    }

}
