package chess.domain.user;

import chess.util.DateTimeConvertUtils;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

public class User {

    private final String name;
    private LocalDateTime createdAt;

    public User(String name) {
        this.name = name;
        this.createdAt = LocalDateTime.now();
    }

    public User(String name, LocalDateTime createdAt) {
        this.name = name;
        this.createdAt = createdAt;
    }

    public User(String name, Timestamp createdAt) {
        this.name = name;
        this.createdAt = DateTimeConvertUtils.toLocalDateTimeFrom(createdAt);
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
