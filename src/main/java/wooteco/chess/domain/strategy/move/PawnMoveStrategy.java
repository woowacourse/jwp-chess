package wooteco.chess.domain.strategy.move;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.Position;

public class PawnMoveStrategy extends MoveStrategy {
    private static final int WHITE_PAWN_MOVE_FORWARD = -1;
    private static final int WHITE_PAWN_MOVE_FORWARD_TWICE = -2;
    private static final int NO_MOVE = 0;
    private static final int PAWN_ATTACK_ABSOLUTE_FILE_VALUE = 1;
    private static final int BLACK_PAWN_MOVE_FORWARD = 1;
    private static final int BLACK_PAWN_MOVE_FORWARD_TWICE = 2;
    private static final int BLACK_START_POSITION = 7;
    private static final int WHITE_START_POSITION = 2;

    @Override
    public boolean checkMovement(Position source, Position target, Board board) {
        Piece sourcePiece = board.getPiece(source);
        Team pawnTeam = sourcePiece.getTeam();

        if (pawnTeam.isWhite()) {
            return checkWhiteMovement(source, target, board);
        }
        return checkBlackMovement(source, target, board);
    }

    private boolean checkWhiteMovement(Position source, Position target, Board board) {
        int fileGap = source.calculateFileGap(target);
        int rankGap = source.calculateRankGap(target);

        if (isWhitePawnMoveForward(fileGap, rankGap)) {
            return board.isEmpty(target);
        }
        if (isWhitePawnStartPosition(fileGap, rankGap)) {
            return source.getRank() == WHITE_START_POSITION && checkObstacle(source, target, board);
        }
        if (isWhitePawnAttack(fileGap, rankGap)) {
            return !board.isEmpty(target) && board.getPiece(target).isBlack();
        }
        return false;
    }

    private boolean checkBlackMovement(Position source, Position target, Board board) {
        int fileGap = source.calculateFileGap(target);
        int rankGap = source.calculateRankGap(target);

        if (isBlackPawnMoveForward(fileGap, rankGap)) {
            return board.isEmpty(target);
        }
        if (isBlackPawnStartPosition(fileGap, rankGap)) {
            return source.getRank() == BLACK_START_POSITION && checkObstacle(source, target, board);
        }
        if (isBlackPawnAttack(fileGap, rankGap)) {
            return !board.isEmpty(target) && board.getPiece(target).isWhite();
        }
        return false;
    }

    private boolean isWhitePawnMoveForward(int fileGap, int rankGap) {
        return fileGap == NO_MOVE && rankGap == WHITE_PAWN_MOVE_FORWARD;
    }

    private boolean isWhitePawnStartPosition(int fileGap, int rankGap) {
        return fileGap == NO_MOVE && rankGap == WHITE_PAWN_MOVE_FORWARD_TWICE;
    }

    private boolean isWhitePawnAttack(int fileGap, int rankGap) {
        return Math.abs(fileGap) == PAWN_ATTACK_ABSOLUTE_FILE_VALUE && rankGap == WHITE_PAWN_MOVE_FORWARD;
    }

    private boolean isBlackPawnMoveForward(int fileGap, int rankGap) {
        return fileGap == NO_MOVE && rankGap == BLACK_PAWN_MOVE_FORWARD;
    }

    private boolean isBlackPawnStartPosition(int fileGap, int rankGap) {
        return fileGap == NO_MOVE && rankGap == BLACK_PAWN_MOVE_FORWARD_TWICE;
    }

    private boolean isBlackPawnAttack(int fileGap, int rankGap) {
        return Math.abs(fileGap) == PAWN_ATTACK_ABSOLUTE_FILE_VALUE && rankGap == BLACK_PAWN_MOVE_FORWARD;
    }
}