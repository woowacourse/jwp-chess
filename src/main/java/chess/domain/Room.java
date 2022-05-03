package chess.domain;

import chess.repository.entity.RoomEntity;
import java.util.Objects;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Room {

    private final long id;
    private final String name;
    private final String password;
    private final String white;
    private final String black;
    private final boolean finished;

    public Room(long id, String name, String password, String white, String black, boolean finished) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.white = white;
        this.black = black;
        this.finished = finished;
    }

    public Room(long id, String name, String password) {
        this(id, name, password, "", "", false);
    }

    public static Room from(RoomEntity roomEntity) {
        return new Room(roomEntity.getId(), roomEntity.getName(), roomEntity.getPassword(), roomEntity.getWhite(),
                roomEntity.getBlack(), roomEntity.isFinished());
    }

    public boolean matchPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.password);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getWhite() {
        return white;
    }

    public String getBlack() {
        return black;
    }

    public boolean isFinished() {
        return finished;
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
