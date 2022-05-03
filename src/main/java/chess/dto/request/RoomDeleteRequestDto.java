package chess.dto.request;

public class RoomDeleteRequestDto {

    private String password;

    public RoomDeleteRequestDto() {
    }

    public RoomDeleteRequestDto(final String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
