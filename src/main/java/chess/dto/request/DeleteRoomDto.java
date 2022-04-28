package chess.dto.request;

public class DeleteRoomDto {
    private final String password;

    public DeleteRoomDto(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "DeleteRoomDto{" +
                ", password='" + password + '\'' +
                '}';
    }
}
