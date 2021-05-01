package chess.domain.game.room;

public class Room {

    private final long id;
    private final String name;
    private final long hostId;
    private final Long guestId;

    public Room(final long id, final String name, final long hostId, final Long guestId) {

        this.id = id;
        this.name = name;
        this.hostId = hostId;
        this.guestId = guestId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getHostId() {
        return hostId;
    }

    public Long getGuestId() {
        return guestId;
    }

}
