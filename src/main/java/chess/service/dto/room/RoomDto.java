package chess.service.dto.room;

import chess.domain.room.Room;

public class RoomDto {

    private Long id;
    private Long gameId;
    private String roomName;
    private String whiteUserName;
    private String blackUserName;

    public RoomDto(Long id, Long gameId, String roomName, String whiteUserName, String blackUserName) {
        this.id = id;
        this.gameId = gameId;
        this.roomName = roomName;
        this.whiteUserName = whiteUserName;
        this.blackUserName = blackUserName;
    }

    public static RoomDto from(Room room, String whiteUserName) {
        return from(room, whiteUserName, "");
    }

    public static RoomDto from(Room room, String whiteUserName, String blackUserName) {
        return new RoomDto(room.getId(), room.gameId(), room.name(), whiteUserName, blackUserName);
    }

    public Long getId() {
        return id;
    }

    public Long getGameId() {
        return gameId;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getWhiteUserName() {
        return whiteUserName;
    }

    public String getBlackUserName() {
        return blackUserName;
    }
}
