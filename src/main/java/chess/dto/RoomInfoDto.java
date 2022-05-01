package chess.dto;

import javax.validation.constraints.Size;

public class RoomInfoDto {
    private static final int MAX_ROOM_INFO_LENGTH = 20;

    @Size(message = "방 제목은 20자가 넘으면 안됩니다.", max = MAX_ROOM_INFO_LENGTH)
    private String title;
    @Size(message = "비밀번호는 20자가 넘으면 안됩니다.", max = MAX_ROOM_INFO_LENGTH)
    private String password;

    public RoomInfoDto() {
    }

    public RoomInfoDto(String title, String password) {
        this.title = title;
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }
}
