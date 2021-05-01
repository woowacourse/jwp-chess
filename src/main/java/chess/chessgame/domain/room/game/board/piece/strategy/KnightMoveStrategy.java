package chess.chessgame.domain.room.game.board.piece.strategy;

import chess.chessgame.domain.room.game.board.position.Direction;

public class KnightMoveStrategy extends DefaultMoveStrategy {
    public KnightMoveStrategy() {
        super(Direction.knightDirection());
    }
}
