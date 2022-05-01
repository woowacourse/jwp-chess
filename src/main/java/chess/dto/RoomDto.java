package chess.dto;

public class RoomDto {

    private String title;
    private String password;

    public RoomDto(String title, String password) {
        validateTitle(title);
        this.title = title;
        this.password = password;
    }

    private void validateTitle(String title) {
        if (title.isBlank()) {
            throw new IllegalArgumentException("방이름을 입력해주세요.");
        }
    }

    public String getPassword() {
        return password;
    }

    public String getTitle() {
        return title;
    }
}
