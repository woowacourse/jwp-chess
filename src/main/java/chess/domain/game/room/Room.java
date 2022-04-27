package chess.domain.game.room;

public class Room {
    private final RoomId id;
    private final RoomTitle roomTitle;
    private final RoomPassword password;

    private Room(RoomId id, RoomTitle roomTitle, RoomPassword password) {
        this.id = id;
        this.roomTitle = roomTitle;
        this.password = password;
    }

    public static Room from(String roomId, String roomTitle, String roomPassword) {
        return new Room(RoomId.from(roomId), RoomTitle.from(roomTitle), RoomPassword.from(roomPassword));
    }

    public static Room create(String roomTitle, String roomPassword) {
        return new Room(RoomId.random(), RoomTitle.from(roomTitle), RoomPassword.from(roomPassword));
    }

    public RoomId getId() {
        return id;
    }

    public RoomTitle getRoomTitle() {
        return roomTitle;
    }

    public RoomPassword getPassword() {
        return password;
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

        if (!id.equals(room.id)) {
            return false;
        }
        return password.equals(room.password);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", roomTitle=" + roomTitle +
                ", password=" + password +
                '}';
    }
}
