package chess.domain.vo;

import java.util.Objects;

public class Room {
    private String name;
    private String password;

    public Room(String name, String password) {
        validateName(name);
        validatePassword(password);
        this.name = name;
        this.password = password;
    }

    public static Room init(String name, String password) {
        return new Room(name, password);
    }

    private void validateName(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("방 이름은 공백일 수 없습니다.");
        }
    }

    private void validatePassword(String password) {
        if (password.isBlank()) {
            throw new IllegalArgumentException("패스워드는 공백일 수 없습니다.");
        }
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Room)) {
            return false;
        }
        Room room = (Room) o;
        return Objects.equals(name, room.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
