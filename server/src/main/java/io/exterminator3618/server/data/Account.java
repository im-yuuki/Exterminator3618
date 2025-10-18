package io.exterminator3618.server.data;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "accounts")
public class Account {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(nullable = false, unique = true)
   private Long id;

   @Column(name = "username", nullable = false, unique = true)
   private String username;

   @Column(name = "pwdHash", nullable = false)
   private String pwdHash;

   @Column(name = "name", nullable = false)
   private String name;

   @Column(name = "xp", nullable = false)
   private int xp = 0;

   @Column(name = "createdAt", nullable = false)
   private LocalDateTime createdAt;

   @Column(name = "lastLoginAt", nullable = true)
   private LocalDateTime lastLoginAt;

   @Column(name = "invisibleMode", nullable = true)
   private boolean invisibleMode;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ban> bans;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Record> records;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
