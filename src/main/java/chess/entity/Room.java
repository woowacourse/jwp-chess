package chess.entity;

public class Room {

    private final Long id;
    private final String state;
    private final String title;
    private final String password;

    public Room(Long id, String state, String title, String password) {
        this.id = id;
        this.state = state;
        this.title = title;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", state='" + state + '\'' +
                ", title='" + title + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
