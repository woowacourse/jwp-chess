package chess.dao.dto;

import chess.domain.game.room.Room;

public class RoomDto {

    private final long id;
    private final long gameId;
    private final long hostId;
    private final long guestId;
    private final String name;
    private final boolean hostParticipated;
    private final boolean guestParticipated;

    private RoomDto(final long id, final long gameId, final long hostId, final long guestId,
        final String name, final boolean hostParticipated, final boolean guestParticipated) {

        this.id = id;
        this.gameId = gameId;
        this.hostId = hostId;
        this.guestId = guestId;
        this.name = name;
        this.hostParticipated = hostParticipated;
        this.guestParticipated = guestParticipated;
    }

    public static RoomDto of(final long id, final long gameId, final long hostId,
        final long guestId, final String name, final boolean hostParticipated,
        final boolean guestParticipated) {

        return new RoomDto(id, gameId, hostId, guestId, name, hostParticipated, guestParticipated);
    }

    public static RoomDto of(final long gameId, final long hostId, final String name) {
        return new RoomDto(
            0L, gameId, hostId, 0L, name, true, false
        );
    }

    public Room toEntity() {
        return new Room(
            id,
            name,
            hostId,
            guestId,
            hostParticipated,
            guestParticipated
        );
    }

    public long getId() {
        return id;
    }

    public long getGameId() {
        return gameId;
    }

    public long getHostId() {
        return hostId;
    }

    public long getGuestId() {
        return guestId;
    }

    public String getName() {
        return name;
    }

    public boolean isHostParticipated() {
        return hostParticipated;
    }

    public boolean isGuestParticipated() {
        return guestParticipated;
    }

}
