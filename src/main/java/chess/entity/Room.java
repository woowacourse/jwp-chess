package chess.entity;

public class Room {

    private final String name;
    private final String turn;
    private long id;

    public Room(long id, String turn, String name) {
        this.id = id;
        this.turn = turn;
        this.name = name;
    }

    public Room(String name) {
        this.name = name;
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
}
