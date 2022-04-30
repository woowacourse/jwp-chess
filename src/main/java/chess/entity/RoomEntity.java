package chess.entity;

public class RoomEntity {

    private long id;
    private final String name;
    private final String turn;
    private final String password;

    public RoomEntity(long id, String turn, String name, String password) {
        this.id = id;
        this.turn = turn;
        this.name = name;
        this.password = password;
    }

    public RoomEntity(String name, String password) {
        this(0L, "empty", name, password);
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
