package chess.domain.strategy;

import chess.domain.ChessBoard;
import chess.domain.position.Position;
import chess.exception.IllegalMoveException;

public final class KingMoveStrategy extends CommonMovingStrategy {

    @Override
    public void isMovable(Position source, Position target, ChessBoard chessBoard) {
        if (source.calculateDistance(target) != 1) {
            throw new IllegalMoveException("한 칸만 이동 가능합니다.");
        }

        checkCommonCondition(source, target, chessBoard);
    }
}
