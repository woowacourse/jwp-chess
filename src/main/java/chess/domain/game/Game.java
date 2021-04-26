package chess.domain.game;

import chess.domain.team.Team;
import chess.domain.user.User;
import java.time.LocalDateTime;

public class Game {

    private final long id;
    private final String name;
    private final User host;
    private final User guest;
    private final Team turn;
    private final boolean isFinished;
    private final LocalDateTime createdTime;

    public Game(final long id, final String name, final User host, final User guest,
        final Team turn, final boolean isFinished, final LocalDateTime createdTime) {

        this.id = id;
        this.name = name;
        this.host = host;
        this.guest = guest;
        this.turn = turn;
        this.isFinished = isFinished;
        this.createdTime = createdTime;
    }

    public String getName() {
        return name;
    }

    public User getHost() {
        return host;
    }

    public User getGuest() {
        return guest;
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

    public long getId() {
        return id;
    }

}

