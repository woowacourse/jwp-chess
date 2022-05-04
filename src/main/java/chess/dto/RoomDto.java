package chess.dto;

public class RoomDto {

    private static final int MAX_TITLE_LENGTH = 20;
    private static final int MAX_PASSWORD_LENGTH = 20;

    private String title;
    private String password;

    public RoomDto(String title, String password) {
        validateTitle(title);
        validatePassword(password);
        this.title = title;
        this.password = password;
    }

    private void validateTitle(String title) {
        if (title.isBlank()) {
            throw new IllegalArgumentException("방 이름을 입력해주세요.");
        }
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException("방 이름의 최대 길이는 20입니다.");
        }
    }

    private void validatePassword(String password) {
        if (password.length() > MAX_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("비밀번호의 최대 길이는 20입니다.");
        }
    }

    public String getPassword() {
        return password;
    }

    public String getTitle() {
        return title;
    }
}
