package chess.view;

public class WebInputView {

    private static final int NAME_LENGTH_LIMIT = 10;
    private static final int PASSWORD_LENGTH_LIMIT = 10;
    private static final String EXCEPTION_NAME_LENGTH_OVER = "[ERROR] 방 이름은 1자 이상 10자 이하여야 합니다.";
    private static final String EXCEPTION_PASSWORD_LENGTH_OVER = "[ERROR] 비밀번호는 1자 이상 10자 이하여야 합니다.";

    public static void validateRoomName(String name) {
        validateRoomNameLength(name);
    }

    private static void validateRoomNameLength(String name) {
        if (name.isEmpty() || name.length() > NAME_LENGTH_LIMIT) {
            throw new IllegalArgumentException(EXCEPTION_NAME_LENGTH_OVER);
        }
    }

    public static void validateRoomPassword(String password) {
        validateRoomPasswordLength(password);
    }

    private static void validateRoomPasswordLength(String password) {
        if (password.isEmpty() || password.length() > PASSWORD_LENGTH_LIMIT) {
            throw new IllegalArgumentException(EXCEPTION_PASSWORD_LENGTH_OVER);
        }
    }
}
