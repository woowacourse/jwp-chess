package chess.websocket.commander.dto;

import chess.domain.room.Room;

public class RoomResponseDto {
    private Long roomId;
    private String title;
    private boolean locked;
    private int playerAmount;

    public RoomResponseDto(Room room) {
        this.roomId = room.id();
        this.title = room.title();
        this.locked = room.isLocked();
        this.playerAmount = room.players().size();
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public int getPlayerAmount() {
        return playerAmount;
    }

    public void setPlayerAmount(int playerAmount) {
        this.playerAmount = playerAmount;
    }
}
