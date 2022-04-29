package chess.dto.request;

public class DeleteRoomDto {
    private final int id;
    private final String password;

    public DeleteRoomDto(int id, String password) {
        this.id = id;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "DeleteRoomDto{" +
            "id=" + id +
            ", password='" + password + '\'' +
            '}';
    }
}
