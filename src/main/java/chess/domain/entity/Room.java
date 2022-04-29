package chess.domain.entity;

public class Room {
    private final Long id;
    private final String title;
    private final String password;

    public Room(Long id, String title, String password) {
        this.id = id;
        this.title = title;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }

    public boolean isNotSamePassword(String password) {
        return !this.password.equals(password);
    }
}
