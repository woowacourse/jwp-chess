package chess.domain.game;

import chess.domain.board.Board;
import chess.domain.game.statistics.GameState;
import chess.dto.GameDataDto;
import chess.dto.GameDto;
import chess.dto.board.WebBoardViewDto;

public abstract class Started implements Game {

    protected final Board board;

    protected Started(Board board) {
        this.board = board;
    }

    @Override
    public GameDto toDtoOf(int gameId) {
        return new GameDto(new GameDataDto(gameId, getState()), new WebBoardViewDto(board));
    }

    protected abstract GameState getState();
}
