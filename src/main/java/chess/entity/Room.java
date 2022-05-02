package chess.entity;

public class Room {
    private final int id;
    private final String turn;
    private final String name;
    private final String password;

    public Room(int id, String turn, String name, String password) {
        this.id = id;
        this.turn = turn;
        this.name = name;
        this.password = password;
    }

    public int getId() {
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
