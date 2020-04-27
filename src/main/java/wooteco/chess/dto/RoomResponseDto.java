package wooteco.chess.dto;

import wooteco.chess.repository.entity.RoomEntity;

public class RoomResponseDto {
    private final int roomId;
    private final String roomName;
    private final String currentColor;

    private RoomResponseDto(int roomId, String roomName, String currentColor) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.currentColor = currentColor;
    }

    public static RoomResponseDto of(RoomEntity roomEntity) {
        return new RoomResponseDto(roomEntity.getRoomId(), roomEntity.getRoomName(), roomEntity.getCurrentColor());
    }

    public int getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getCurrentColor() {
        return currentColor;
    }
}
