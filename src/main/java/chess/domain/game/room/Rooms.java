package chess.domain.game.room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Rooms {

    private final List<Room> rooms;

    public Rooms(final List<Room> rooms) {
        this.rooms = new ArrayList<>(rooms);
    }

    public List<Room> toList() {
        return Collections.unmodifiableList(rooms);
    }

}
