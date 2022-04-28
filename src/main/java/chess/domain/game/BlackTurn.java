package chess.domain.game;

import chess.domain.board.Board;

final class BlackTurn extends Running {

    BlackTurn(Board board) {
        super(GameState.BLACK_TURN, board);
    }

    @Override
    protected Game continueGame() {
        return new WhiteTurn(board);
    }
}
