package chess.entity;

public class Room {

    private final String name;
    private final String turn;
    private long id;
    private final String password;

    public Room(long id, String password, String turn, String name) {
        this.id = id;
        this.password = password;
        this.turn = turn;
        this.name = name;
    }

    public Room(String name, String password) {
        this.name = name;
        this.password = password;
        this.turn = "empty";
    }

    public long getId() {
        return id;
    }

    public String getTurn() {
        return turn;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
