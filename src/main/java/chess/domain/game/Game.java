package chess.domain.game;

import chess.domain.team.Team;
import java.time.LocalDateTime;

public class Game {

    private final long id;
    private final String name;
    private final long hostId;
    private final long guestId;
    private final Team turn;
    private final boolean isFinished;
    private final LocalDateTime createdTime;

    public Game(final long id, final String name, final long hostId, final long guestId,
        final Team turn, final boolean isFinished,
        final LocalDateTime createdTime) {
        this.id = id;
        this.name = name;
        this.hostId = hostId;
        this.guestId = guestId;
        this.turn = turn;
        this.isFinished = isFinished;
        this.createdTime = createdTime;
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

    public long getGuestId() {
        return guestId;
    }

    public Team getTurn() {
        return turn;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }
}

