package chess.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {

    private final String id;
    private final String name;
    private final String password;
    private final LocalDateTime createdDate;

    public User(final String name, final String password) {
        this(UUID.randomUUID().toString(), name, password, LocalDateTime.now());
    }

    public User(final String id, final String name, final String password, final LocalDateTime createdDate) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
