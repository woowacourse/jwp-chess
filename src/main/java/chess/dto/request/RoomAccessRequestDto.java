package chess.dto.request;

public class RoomAccessRequestDto {

    private String password;

    public RoomAccessRequestDto(String password) {
        this.password = password;
    }

    private RoomAccessRequestDto() {
    }

    public String getPassword() {
        return password;
    }
}
