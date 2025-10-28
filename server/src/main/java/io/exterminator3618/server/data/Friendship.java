package io.exterminator3618.server.data;

import io.exterminator3618.server.utils.FriendshipId;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "friendships")
@IdClass(FriendshipId.class)
@Data
public class Friendship {

    @Id
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Account account1;

    @Id
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Account account2;

    @Column(nullable = false)
    private LocalDateTime since;

    @PrePersist
    protected void onCreate() {
        since = LocalDateTime.now();
    }

}
