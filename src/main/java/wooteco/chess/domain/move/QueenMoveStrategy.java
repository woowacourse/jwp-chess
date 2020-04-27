package wooteco.chess.domain.move;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.position.Position;

public class QueenMoveStrategy extends MoveStrategy {
    @Override
    public boolean checkMovement(Position source, Position target, Board board) {
        return new RookMoveStrategy().movable(source, target, board)
                || new BishopMoveStrategy().movable(source, target, board);
    }
}