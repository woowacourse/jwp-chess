package chess.domain.game;

import chess.domain.board.Board;
import chess.domain.board.piece.Color;

final class BlackTurn extends Running {

    BlackTurn(Board board) {
        super(GameState.BLACK_TURN, board);
    }

    @Override
    public boolean isValidTurn(Color playerColor) {
        return playerColor.equals(Color.BLACK);
    }

    @Override
    protected Game continueGame() {
        return new WhiteTurn(board);
    }
}
