package chess.web.dto.game.room;

import chess.domain.game.room.Room;
import chess.domain.user.User;
import chess.web.dto.user.UserResponseDto;

public class RoomResponseDto {

    private final long id;
    private final String name;
    private final UserResponseDto host;
    private final UserResponseDto guest;

    private RoomResponseDto(final long id, final String name, final UserResponseDto host,
        final UserResponseDto guest) {

        this.id = id;
        this.name = name;
        this.host = host;
        this.guest = guest;
    }

    public static RoomResponseDto of(final Room room, final User host, final User guest) {
        return new RoomResponseDto(
            room.getId(),
            room.getName(),
            UserResponseDto.from(host),
            UserResponseDto.from(guest)
        );
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UserResponseDto getHost() {
        return host;
    }

    public UserResponseDto getGuest() {
        return guest;
    }

}
