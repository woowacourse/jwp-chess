package chess.domain;

public class Room {

    public static final int NAME_LENGTH_LIMIT = 16;
    public static final int PASSWORD_LENGTH_LIMIT = 16;

    private final String name;
    private final String password;

    public Room(String name, String password) {
        validateName(name);
        validatePassword(password);
        this.name = name;
        this.password = password;
    }

    private void validateName(String name) {
        if (name.isBlank() || name.length() > NAME_LENGTH_LIMIT) {
            throw new IllegalArgumentException("방 이름은 최소 1글자, 최대 16글자입니다.");
        }
    }

    private void validatePassword(String password) {
        if (password.isBlank() || password.length() > PASSWORD_LENGTH_LIMIT) {
            throw new IllegalArgumentException("비밀번호는 최소 1글자, 최대 16글자입니다.");
        }
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
