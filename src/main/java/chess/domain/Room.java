package chess.domain;

import java.util.Objects;

public class Room {

    private static final String EMPTY_PASSWORD = "";

    private final long id;
    private final String password;

    public Room(long id) {
        this(id, EMPTY_PASSWORD);
    }

    public Room(long id, String password) {
        this.id = id;
        this.password = password;
    }

    public boolean checkPassword(final String password) {
        return this.password.equals(password);
    }

    public long getId() {
        return id;
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
        return id == room.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
