package chess.domain.game;

import chess.domain.board.Board;
import chess.domain.board.piece.Color;

final class WhiteTurn extends Running {

    WhiteTurn(Board board) {
        super(GameState.WHITE_TURN, board);
    }

    @Override
    public Color getCurrentTurnColor() {
        return Color.WHITE;
    }

    @Override
    protected Game continueGame() {
        return new BlackTurn(board);
    }
}
