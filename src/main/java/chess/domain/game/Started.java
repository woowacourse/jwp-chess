package chess.domain.game;

import chess.domain.board.Board;
import chess.domain.game.statistics.GameState;
import chess.dto.GameStateDto;
import chess.dto.GameSnapshotDto;
import chess.dto.board.WebBoardViewDto;

public abstract class Started implements Game {

    protected final Board board;

    protected Started(Board board) {
        this.board = board;
    }

    @Override
    public GameSnapshotDto toDtoOf(int gameId) {
        return new GameSnapshotDto(new GameStateDto(gameId, getState()), new WebBoardViewDto(board));
    }

    protected abstract GameState getState();
}
