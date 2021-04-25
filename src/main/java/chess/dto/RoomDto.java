package chess.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RoomDto {

    @Size(min = 3, max = 12, message = "적절하지 않은 방 이름 길이")
    private String roomName;

    @NotNull
    @NotBlank
    private String player1;
    private String player2;

    public RoomDto() {
    }

    public String getRoomName() {
        return roomName;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @Override
    public String toString() {
        return "RoomDto{" +
                "roomName='" + roomName + '\'' +
                ", player1='" + player1 + '\'' +
                ", player2='" + player2 + '\'' +
                '}';
    }
}
