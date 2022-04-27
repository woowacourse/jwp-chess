package chess.entity;

public class Room {

    private long id;
    private final String name;
    private final String turn;
    private final String password;

    public Room(long id, String turn, String name, String password) {
        this.id = id;
        this.turn = turn;
        this.name = name;
        this.password = password;
    }

    public Room(String name, String password) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("방 제목은 빈 문자열일 수 없습니다.");
        }
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
