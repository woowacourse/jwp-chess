package chess.chessgame.domain.piece.strategy;

import chess.chessgame.domain.position.Direction;

public class KnightMoveStrategy extends DefaultMoveStrategy {
    public KnightMoveStrategy() {
        super(Direction.knightDirection());
    }
}
