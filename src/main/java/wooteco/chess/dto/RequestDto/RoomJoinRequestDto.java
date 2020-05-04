package wooteco.chess.dto.RequestDto;

import wooteco.chess.entity.Room;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RoomJoinRequestDto {

    @NotBlank(message = "name is mandatory")
    @Size(max=21, message = "password size must be less than 20 characters")
    private String roomName;

    @NotBlank(message = "password is mandatory")
    @Size(max=21, message = "password size must be less than 20 characters")
    private String userPassword;

    public RoomJoinRequestDto(String userPassword, String roomName) {
        this.userPassword = userPassword;
        this.roomName = roomName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getRoomName() {
        return roomName;
    }

    public Room toEntity() {
        Room room = new Room();
        room.setBlackPassword(userPassword);
        room.setName(roomName);
        return room;
    }
}
