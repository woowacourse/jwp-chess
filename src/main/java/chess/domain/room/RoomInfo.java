package chess.domain.room;

public class RoomInfo {
    private final String name;
    private final String pw;
    private final Long gameId;

    public RoomInfo(final String name, final String pw, final Long gameId) {
        this.name = name;
        this.pw = pw;
        this.gameId = gameId;
    }

    public boolean checkPassword(RoomInfo roomInfo) {
        System.out.println("checkPassword : " + roomInfo.getPw());
        System.out.println("checkPassword : " + pw);
        return roomInfo.getPw().equals(pw);
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

    @Override
    public String toString() {
        return "RoomInfo{" +
                "name='" + name + '\'' +
                ", pw='" + pw + '\'' +
                ", gameId=" + gameId +
                '}';
    }
}
