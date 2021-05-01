package chess.chessgame.domain.room.game.board.piece.strategy;

import chess.chessgame.domain.room.game.board.position.Direction;

public class BishopMoveStrategy extends DefaultMoveStrategy {
    public BishopMoveStrategy() {
        super(Direction.diagonalDirection());
    }
}
