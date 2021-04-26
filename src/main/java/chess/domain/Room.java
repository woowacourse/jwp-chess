package chess.domain;

public class Room {
    private long id;
    private String name;
    private String pw;
    private long gameId;

    public Room() {}

    public Room(long id, String name, String pw, long gameId) {
        this.id = id;
        this.name = name;
        this.pw = pw;
        this.gameId = gameId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPw() {
        return pw;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(final long gameId) {
        this.gameId = gameId;
    }

    public void checkPassword(Room room) {
        if (!room.getPw().equals(pw)) {
            throw new IllegalArgumentException("비밀번호가 다릅니다.");
        }
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pw='" + pw + '\'' +
                ", gameId='" + gameId + '\'' +
                '}';
    }
}
