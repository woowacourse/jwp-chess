package chess.dto.request;

public class CreateRoomDto {
    private String title;
    private String password;

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "CreateRoomDto{" +
                "title='" + title + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
