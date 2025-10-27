package io.exterminator3618.server.utils;

import java.io.Serializable;
import java.util.Objects;

public class FriendshipId implements Serializable {

    private Long account1;
    private Long account2;

    public FriendshipId() {
    }

    public FriendshipId(Long account1, Long account2) {
        if (account1 == null || account2 == null) {
            throw new IllegalArgumentException("User IDs cannot be null");
        }
        if (account1.equals(account2)) {
            throw new IllegalArgumentException("Cannot form a friendship with oneself");
        }

        // account1 < account2
        if (account1 < account2) {
            this.account1 = account1;
            this.account2 = account2;
        } else {
            this.account1 = account2;
            this.account2 = account1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendshipId that = (FriendshipId) o;
        return Objects.equals(account1, that.account1) &&
                Objects.equals(account2, that.account2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account1, account2);
    }

}