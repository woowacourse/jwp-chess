package chess.domain.game;

import chess.domain.board.Board;
import chess.domain.location.Location;
import chess.domain.team.Team;
import java.time.LocalDateTime;

public class Game {

    private final long id;
    private final String name;
    private final Team turn;
    private final boolean isFinished;
    private final LocalDateTime createdTime;
    private final Board board;

    public Game(final long id, final String name, final Team turn, final boolean isFinished,
        final LocalDateTime createdTime, final Board board) {
        this.id = id;
        this.name = name;
        this.turn = turn;
        this.isFinished = isFinished;
        this.createdTime = createdTime;
        this.board = board;
    }

    public boolean checkMovement(final String source, final String target, final Team turn) {
        if (!this.turn.equals(turn)) {
            return false;
        }

        return board.isMovable(
            Location.convert(source),
            Location.convert(target),
            turn
        );
    }

    public void move(final String source, final String target, final Team turn) {
        board.move(
            Location.convert(source),
            Location.convert(target),
            turn
        );
    }

}
