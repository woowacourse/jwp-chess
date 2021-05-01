package chess.controller.room;

class RoomResponseDto {
    private final boolean isFull;
    private final String roomName;

    public RoomResponseDto(boolean isFull, String roomName) {
        this.isFull = isFull;
        this.roomName = roomName;
    }

    public boolean isIsFull() {
        return isFull;
    }

    public String getRoomName() {
        return roomName;
    }
}
