package chess.entity;

import static chess.domain.piece.Color.*;

import java.util.Objects;

public class Room {

    private static final int ID_UNASSIGNED = 0;
    private final long id;
    private final String turn;
    private final String name;
    private final String password;

    public Room(long id, String name, String password, String turn) {
        validateName(name);
        validatePassword(password);
        this.id = id;
        this.name = name;
        this.password = password;
        this.turn = turn;
    }

    public Room(String name, String password) {
        this(ID_UNASSIGNED, name, password, EMPTY.getName());
    }

    public Room(long id, String name, String turn) {
        this(id, name, null, turn);
    }

    private void validateName(String name) {
        int length = name.length();
        if (length < 1 || length > 20) {
            throw new IllegalArgumentException("방 제목은 1 ~ 20자만 가능합니다.");
        }
    }

    private void validatePassword(String password) {
        if (Objects.isNull(password)) {
            return;
        }
        int length = password.length();
        if (length < 10 || length > 20) {
            throw new IllegalArgumentException("비밀번호는 10 ~ 20자만 가능합니다.");
        }
    }

    public long getId() {
        return id;
    }

    public String getTurn() {
        return turn;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
