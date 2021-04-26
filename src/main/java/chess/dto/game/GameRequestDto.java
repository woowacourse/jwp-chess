package chess.dto.game;

import chess.dao.dto.GameDto;
import java.time.LocalDateTime;

public class GameRequestDto {

    private static final String EMPTY_STRING = "";

    private final String name;
    private final long hostId;
    private final long guestId;

    public GameRequestDto(final String name, final long hostId, final long guestId) {
        this.name = name;
        this.hostId = hostId;
        this.guestId = guestId;
    }

    public GameDto toGameDto() {
        return new GameDto(
            0L, name, hostId, guestId, EMPTY_STRING, false, LocalDateTime.now()
        );
    }

    public String getName() {
        return name;
    }

    public long getHostId() {
        return hostId;
    }

    public long getGuestId() {
        return guestId;
    }

}
