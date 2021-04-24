package chess.controller.dto;

import javax.validation.constraints.Size;

public class RoomDto {

    @Size(min = 3, max = 12, message = "적절하지 않은 방 이름 길이")
    private String roomName;
    private String player1;

    public RoomDto(){

    }

    public String getRoomName() {
        return roomName;
    }

    public String getPlayer1() {
        return player1;
    }
}
