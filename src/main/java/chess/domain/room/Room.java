package chess.domain.room;

public class Room {
    private final Long id;
    private final RoomInfo roomInfo;
    private final Players players;

    public Room(Long id, RoomInfo roomInfo, Players players) {
        this.id = id;
        this.roomInfo = roomInfo;
        this.players = players;
    }

    public boolean checkPassword(RoomInfo roomInfo) {
        return this.roomInfo.checkPassword(roomInfo);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return roomInfo.getName();
    }

    public String getPw() {
        return roomInfo.getPw();
    }

    public Long getGameId() {
        return roomInfo.getGameId();
    }

    public String getWhitePlayer() {
        return players.getWhitePlayer();
    }

    public String getBlackPlayer() {
        return players.getBlackPlayer();
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public boolean enterable() {
       return players.enterable();
    }

    public boolean isWhitePlayer(String user) {
        return user.equals(players.getWhitePlayer());
    }

    public boolean isBlackPlayer(String user) {
        return user.equals(players.getBlackPlayer());
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", roomInfo=" + roomInfo +
                ", players=" + players +
                '}';
    }
}
