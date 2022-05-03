package chess.domain;

public class Room {
    private final String title;
    private final String password;

    public Room(String title, String password) {
        this.title = title;
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }
}
