package chess.domain.user;

import java.time.LocalDateTime;

public class User {

    private final long id;
    private final String name;
    private final String password;
    private LocalDateTime createdTime;

    public User(long id, String name, String password, LocalDateTime createdTime) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.createdTime = createdTime;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", password='" + password + '\'' +
            ", createdTime=" + createdTime +
            '}';
    }
}
