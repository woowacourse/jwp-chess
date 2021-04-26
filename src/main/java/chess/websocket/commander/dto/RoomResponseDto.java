package chess.websocket.commander.dto;

import chess.domain.room.Room;

public class RoomResponseDto {
    private String title;
    private boolean locked;
    private int playerAmount;

    public RoomResponseDto(Room room) {
        this.title = room.title();
        this.locked = room.isLocked();
        this.playerAmount = room.count();
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
