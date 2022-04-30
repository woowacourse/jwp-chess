package chess.domain.game;

import chess.domain.piece.Color;
import chess.dto.LogInDto;

public class Room {
    private static final String INCORRECT_PASSWORD_ERROR_MESSAGE = "올바르지 않은 비밀번호 입니다.";

    private final String id;
    private final String password;
    private final boolean isEnd;
    private final Color turn;

    public Room(String id, String password, Color turn, boolean isEnd) {
        this.id = id;
        this.password = password;
        this.turn = turn;
        this.isEnd = isEnd;
    }

    public Room(String id, Color turn, boolean isEnd) {
        this.id = id;
        this.password = "";
        this.turn = turn;
        this.isEnd = isEnd;
    }

    public void validateLogInPassword(LogInDto logInDto) {
        if (!password.equals(logInDto.getPassword())) {
            throw new IllegalArgumentException(INCORRECT_PASSWORD_ERROR_MESSAGE);
        }
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public Color getTurn() {
        return turn;
    }

    public boolean isEnd() {
        return isEnd;
    }
}
