package chess.domain;

import chess.exception.UserInputException;

public class Room {

    private static final int NAME_MIN_SIZE = 1;
    private static final int NAME_MAX_SIZE = 16;

    private int id;
    private final String name;
    private final String password;
    private boolean end;

    public Room(int id, Room room) {
        this.id = id;
        this.name = room.getName();
        this.password = room.getPassword();
        this.end = room.getEnd();
    }

    public Room(String name, String password, boolean end) {
        validateNameSize(name);
        validatePassword(password);
        this.name = name;
        this.password = password;
        this.end = end;
    }

    private void validateNameSize(String name) {
        if (name.length() < NAME_MIN_SIZE || name.length() > NAME_MAX_SIZE) {
            throw new UserInputException("방 이름은 1자 이상, 16자 이하입니다.");
        }
    }

    private void validatePassword(String password) {
        if (password.isBlank()) {
            throw new UserInputException("비밀번호를 입력하세요");
        }
    }

    public boolean isRightPassword(String password) {
        return this.password.equals(password);
    }

    public void finish() {
        this.end = true;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public boolean getEnd() {
        return end;
    }
}
