package chess.domain;

import java.util.ArrayList;
import java.util.List;

public class User {
    private List<Long> roomIds;

    public User() {
        roomIds = new ArrayList<>();
    }

    public void addRoomId(Long roomId) {
        roomIds.add(roomId);
        System.out.println(roomIds);
    }

    public boolean hasRoomId(long roomId) {
        System.out.println(roomIds);
        return roomIds.contains(roomId);
    }
}
