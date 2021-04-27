package chess.domain.room;

import static chess.domain.room.RoomStatus.*;

public class Room {
    private final Long id;
    private final RoomInfo roomInfo;
    private final Players players;
    private final RoomStatus status;

    public Room(Long id, RoomInfo roomInfo, Players players) {
        this.id = id;
        this.roomInfo = roomInfo;
        this.players = players;
        this.status = READY;
    }

    public Room(Long id, RoomInfo roomInfo, Players players, RoomStatus roomStatus) {
        this.id = id;
        this.roomInfo = roomInfo;
        this.players = players;
        this.status = roomStatus;
    }

    public boolean checkPassword(RoomInfo roomInfo) {
        return roomInfo.checkPassword(roomInfo);
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
}
