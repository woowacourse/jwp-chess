package chess.domain.game;

import chess.domain.board.Board;
import chess.domain.game.statistics.GameState;
import chess.dto.view.GameSnapshotDto;
import chess.dto.view.board.BoardViewDto;

public abstract class Started implements Game {

    protected final Board board;

    protected Started(Board board) {
        this.board = board;
    }

    @Override
    public GameSnapshotDto toSnapshotDto() {
        return new GameSnapshotDto(getState(), new BoardViewDto(board));
    }

    protected abstract GameState getState();
}
