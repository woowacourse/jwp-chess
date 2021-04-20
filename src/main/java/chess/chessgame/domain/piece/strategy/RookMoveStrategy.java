package chess.chessgame.domain.piece.strategy;

import chess.chessgame.domain.position.Direction;

public class RookMoveStrategy extends DefaultMoveStrategy {
    public RookMoveStrategy() {
        super(Direction.linearDirection());
    }
}
