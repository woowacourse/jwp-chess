package chess.domain;

public class Room {
    private Long id;
    private String name;
    private String pw;
    private Long gameId;

    public Room() {}

    public Room(Long id, String name, String pw, Long gameId) {
        this.id = id;
        this.name = name;
        this.pw = pw;
        this.gameId = gameId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPw() {
        return pw;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(final Long gameId) {
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
