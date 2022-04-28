package chess.domain.game;

import chess.domain.board.Board;

final class WhiteTurn extends Running {

    WhiteTurn(Board board) {
        super(GameState.WHITE_TURN, board);
    }

    @Override
    protected Game continueGame() {
        return new BlackTurn(board);
    }
}
