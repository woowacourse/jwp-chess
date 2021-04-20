package chess.chessgame.domain.piece.strategy;

import chess.chessgame.domain.position.Direction;

public class BishopMoveStrategy extends DefaultMoveStrategy {
    public BishopMoveStrategy() {
        super(Direction.diagonalDirection());
    }
}
