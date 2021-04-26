package chess.dto.game;

import chess.domain.game.Game;
import chess.domain.team.Team;
import java.time.LocalDateTime;

public class GameRequestDto {

    private final String name;
    private final long hostId;
    private final long guestId;

    public GameRequestDto(String name, long hostId, long guestId) {
        this.name = name;
        this.hostId = hostId;
        this.guestId = guestId;
    }

    public Game toEntity() {
        return new Game(0L, name, hostId, guestId, Team.WHITE, false, LocalDateTime.now());
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
