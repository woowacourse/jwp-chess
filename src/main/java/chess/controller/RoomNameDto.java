package chess.controller;

public class RoomNameDto {
    private String roomName;

    public RoomNameDto(){
    }

    public RoomNameDto(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
