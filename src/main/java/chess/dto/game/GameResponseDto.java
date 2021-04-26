package chess.dto.game;

import chess.domain.game.Game;
import chess.domain.team.Team;

public class GameResponseDto {

    private final String name;
    private final Team turn;
    private final long hostId;
    private final long guestId;
    private final boolean isFinished;

    public GameResponseDto(final String name, final Team turn, final long hostId,
        final long guestId, final boolean isFinished) {
        this.name = name;
        this.turn = turn;
        this.hostId = hostId;
        this.guestId = guestId;
        this.isFinished = isFinished;
    }

    public static GameResponseDto from(Game game) {
        return new GameResponseDto(game.getName(), game.getTurn(), game.getHostId(),
            game.getGuestId(), game.isFinished());
    }

    public String getName() {
        return name;
    }

    public Team getTurn() {
        return turn;
    }

    public long getHostId() {
        return hostId;
    }

    public long getGuestId() {
        return guestId;
    }

    public boolean isFinished() {
        return isFinished;
    }
}
