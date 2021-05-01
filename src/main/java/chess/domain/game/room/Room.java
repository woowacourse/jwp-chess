package chess.domain.game.room;

public class Room {

    private final long id;
    private final String name;
    private final long hostId;
    private final Long guestId;
    private final boolean hostParticipated;
    private final boolean guestParticipated;

    public Room(final long id, final String name, final long hostId, final Long guestId,
        final boolean hostParticipated, final boolean guestParticipated) {

        this.id = id;
        this.name = name;
        this.hostId = hostId;
        this.guestId = guestId;
        this.hostParticipated = hostParticipated;
        this.guestParticipated = guestParticipated;
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

    public boolean isHostParticipated() {
        return hostParticipated;
    }

    public boolean isGuestParticipated() {
        return guestParticipated;
    }

}
