package chess.domain.user;

import java.time.LocalDateTime;

public class User {

    private final long id;
    private final String name;
    private final String password;
    private final LocalDateTime createdTime;

    public User(final long id, final String name, final String password,
        final LocalDateTime createdTime) {

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

}
