package chess.domain.game.room;

import java.util.UUID;

public class RoomId {
    private final String roomId;

    private RoomId(String roomId) {
        this.roomId = roomId;
    }

    public static RoomId from(String gameId) {
        return new RoomId(gameId);
    }

    public static RoomId random() {
        String uuid = UUID.randomUUID().toString();
        return new RoomId(uuid);
    }

    public String getValue() {
        return roomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RoomId roomId1 = (RoomId) o;

        return roomId.equals(roomId1.roomId);
    }

    @Override
    public int hashCode() {
        return roomId.hashCode();
    }

    @Override
    public String toString() {
        return "RoomId{" +
                "roomId='" + roomId + '\'' +
                '}';
    }
}
