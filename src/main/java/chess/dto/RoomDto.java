package chess.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RoomDto {

    private Long id;

    @Size(min = 3, max = 12, message = "적절하지 않은 방 이름 길이")
    private String roomName;

    @NotNull
    @NotBlank
    private String player1;
    private String player2;

    public RoomDto() {
    }

    public RoomDto(final Long id, final String name) {
        this.id = id;
        this.roomName = name;
    }

    public RoomDto(final String roomName, final String player1, final String player2) {
        this.roomName = roomName;
        this.player1 = player1;
        this.player2 = player2;
    }

    public Long getId() {
        return id;
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
}
