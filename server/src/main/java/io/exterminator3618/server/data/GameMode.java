package io.exterminator3618.server.data;

import jakarta.persistence.Entity;
import jakarta.persistence.*;

@Entity
@Table(name = "gamemodes")
public class GameMode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

}
