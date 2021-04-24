package chess.chessgame.domain.room.game.board.piece.strategy;

import chess.chessgame.domain.room.game.board.position.Direction;

public class QueenMoveStrategy extends DefaultMoveStrategy {
    public QueenMoveStrategy() {
        super(Direction.everyDirection());
    }
}
