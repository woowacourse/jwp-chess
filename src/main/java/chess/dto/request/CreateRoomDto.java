package chess.dto.request;

public class CreateRoomDto {
    private final String title;
    private final String password;

    public CreateRoomDto(String title, String password) {
        this.title = title;
        this.password = password;
    }

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
