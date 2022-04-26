package chess.domain.game;

import chess.domain.board.Board;
import chess.dto.view.GameSnapshotDto;
import chess.dto.view.board.BoardViewDto;
import java.util.Objects;

public abstract class Started implements Game {

    protected final GameState state;
    protected final Board board;

    protected Started(GameState state, Board board) {
        this.state = state;
        this.board = board;
    }

    @Override
    public GameSnapshotDto toSnapshotDto() {
        return new GameSnapshotDto(state, new BoardViewDto(board));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Started started = (Started) o;
        return state == started.state
                && Objects.equals(board, started.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, board);
    }

    @Override
    public String toString() {
        return "Game{" + "state=" + state + ", board=" + board + '}';
    }
}
