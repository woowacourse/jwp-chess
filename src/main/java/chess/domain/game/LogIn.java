package chess.domain.game;

import java.util.Objects;
import java.util.regex.Pattern;

public class LogIn {
    private static final Pattern logInPattern = Pattern.compile("^[a-zA-Z가-힣ㄱ-ㅎ0-9]*$");
    private static final String LOGIN_FORMAT_ERROR_MESSAGE = "방 제목, 패스워드는 한글,영어,숫자 이외엔 들어올 수 없습니다.";

    private final String id;
    private final String password;

    public LogIn(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public void validateFormat() {
        if (!logInPattern.matcher(id).matches()) {
            throw new IllegalArgumentException(LOGIN_FORMAT_ERROR_MESSAGE);
        }
        if (!logInPattern.matcher(password).matches()) {
            throw new IllegalArgumentException(LOGIN_FORMAT_ERROR_MESSAGE);
        }
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LogIn logIn = (LogIn) o;
        return Objects.equals(id, logIn.id) && Objects.equals(password, logIn.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, password);
    }

    @Override
    public String toString() {
        return "LogInDto{" +
                "gameId='" + id + '\'' +
                ", gamePassword='" + password + '\'' +
                '}';
    }
}
