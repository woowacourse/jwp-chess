package chess.domain.user;

import java.time.LocalDateTime;

public class User {

    private static final User EMPTY_USER = new User(0, "", "", LocalDateTime.MIN);

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

    public static User empty() {
        return EMPTY_USER;
    }

    public void checkPassword(final String password) {
        if (!this.password.equals(password)) {
            throw new WrongPasswordException(password);
        }
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
