package chess.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Rooms {

    private final Map<String, Game> rooms;

    public Rooms() {
        this.rooms = new HashMap<>();
    }

    public void addRoom(String name, Game game) {
        rooms.put(name, game);
    }

    public Optional<Game> findGame(String name) {
        if (rooms.containsKey(name)) {
            return Optional.of(rooms.get(name));
        }
        return Optional.empty();
    }

    public void deleteRoom(String roomName) {
        rooms.remove(roomName);
    }
}
