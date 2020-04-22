package wooteco.chess.domain.strategy.move;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.Position;

public class PawnMoveStrategy extends MoveStrategy {
    private static final int WHITE_START_POSITION = 2;
    private static final int BLACK_START_POSITION = 7;

    @Override
    public boolean checkMovement(Position source, Position target, Board board) {

        int fileGap = source.calculateFileGap(target);
        int rankGap = source.calculateRankGap(target);

        Piece sourcePiece = board.getPiece(source);
        Piece targetPiece = board.getPiece(target);
        Team pawnTeam = sourcePiece.getTeam();

        if (pawnTeam.isWhite()) {
            if (isWhitePawnMoveForward(fileGap, rankGap)) {
                return board.isEmpty(target);
            }
            if (isWhitePawnStartPosition(fileGap, rankGap)) {
                return source.getRank() == WHITE_START_POSITION;
            }
            if (isWhitePawnAttack(fileGap, rankGap)) {
                return !board.isEmpty(target) && sourcePiece.isEnemy(targetPiece);
            }
        }
        if (!pawnTeam.isWhite()) {
            if (isBlackPawnMoveForward(fileGap, rankGap)) {
                return board.isEmpty(target);
            }
            if (isBlackPawnStartPosition(fileGap, rankGap)) {
                return source.getRank() == BLACK_START_POSITION;
            }
            if (isBlackPawnAttack(fileGap, rankGap)) {
                return !board.isEmpty(target) && sourcePiece.isEnemy(targetPiece);
            }
        }
        return false;
    }

    private boolean isWhitePawnMoveForward(int fileGap, int rankGap) {
        return fileGap == 0 && rankGap == -1;
    }

    private boolean isWhitePawnStartPosition(int fileGap, int rankGap) {
        return fileGap == 0 && rankGap == -2;
    }

    private boolean isWhitePawnAttack(int fileGap, int rankGap) {
        return Math.abs(fileGap) == 1 && rankGap == -1;
    }

    private boolean isBlackPawnMoveForward(int fileGap, int rankGap) {
        return fileGap == 0 && rankGap == 1;
    }

    private boolean isBlackPawnStartPosition(int fileGap, int rankGap) {
        return fileGap == 0 && rankGap == 2;
    }

    private boolean isBlackPawnAttack(int fileGap, int rankGap) {
        return Math.abs(fileGap) == 1 && rankGap == 1;
    }
}