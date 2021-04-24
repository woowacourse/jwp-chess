package chess.chessgame.domain.room.game.board.piece.strategy;

import chess.chessgame.domain.room.game.board.position.Direction;

public class RookMoveStrategy extends DefaultMoveStrategy {
    public RookMoveStrategy() {
        super(Direction.linearDirection());
    }
}
