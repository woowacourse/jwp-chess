package chess.entity;

import chess.domain.game.LogIn;
import chess.domain.piece.Color;
import java.util.Objects;

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

    public void validateLogInPassword(LogIn logIn) {
        if (!password.equals(logIn.getPassword())) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Room room = (Room) o;
        return isEnd == room.isEnd && Objects.equals(id, room.id) && Objects.equals(password,
                room.password) && turn == room.turn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, password, isEnd, turn);
    }
}
