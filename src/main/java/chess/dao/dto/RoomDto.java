package chess.dao.dto;

import chess.domain.game.room.Room;

public class RoomDto {

    private final long id;
    private final long gameId;
    private final long hostId;
    private final Long guestId;
    private final String name;

    private RoomDto(final long id, final long gameId, final long hostId, final Long guestId,
        final String name) {

        this.id = id;
        this.gameId = gameId;
        this.hostId = hostId;
        this.guestId = guestId;
        this.name = name;
    }

    public static RoomDto of(final long id, final long gameId, final long hostId,
        final Long guestId, final String name) {

        return new RoomDto(id, gameId, hostId, guestId, name);
    }

    public static RoomDto of(final long gameId, final long hostId, final String name) {
        return new RoomDto(
            0L, gameId, hostId, 0L, name
        );
    }

    public Room toEntity() {
        return new Room(
            id,
            name,
            hostId,
            guestId
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

}
