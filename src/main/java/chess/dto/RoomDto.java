package chess.dto;

import java.time.LocalDateTime;

public class RoomDto {
    private final long roomId;
    private final String roomName;
    private final LocalDateTime createdAt;

    public RoomDto(long roomId, String roomName, LocalDateTime createdAt) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.createdAt = createdAt;
    }

    public long getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
