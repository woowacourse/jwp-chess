package chess.chessgame.domain.piece.strategy;

import chess.chessgame.domain.position.Direction;

public class QueenMoveStrategy extends DefaultMoveStrategy {
    public QueenMoveStrategy() {
        super(Direction.everyDirection());
    }
}
