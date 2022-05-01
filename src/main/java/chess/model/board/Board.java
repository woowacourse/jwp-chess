package chess.model.board;

import chess.model.game.GameResult;
import chess.model.piece.Empty;
import chess.model.piece.Piece;
import chess.model.strategy.move.Direction;
import chess.model.strategy.move.MoveType;
import java.util.HashMap;
import java.util.Map;

public final class Board {

    private final Map<Square, Piece> board;

    public Board(final Map<Square, Piece> board) {
        this.board = new HashMap<>(board);
    }

    public Board(final BoardInitializer boardInitializer) {
        this(boardInitializer.initPieces());
    }

    public static Board init() {
        return new Board(new ChessInitializer());
    }

    public Piece findPieceBySquare(final Square square) {
        if (board.containsKey(square)) {
            return board.get(square);
        }
        throw new IllegalArgumentException("해당 위치의 값을 찾을 수 없습니다.");
    }

    public MoveResult move(final Square from, final Square to) {
        Piece movePiece = findPieceBySquare(from);
        Piece targetPiece = findPieceBySquare(to);
        checkCanMove(from, to, movePiece, targetPiece);
        checkHasPieceInRoute(from, to);
        updateBoard(from, to, movePiece);
        return MoveResult.from(from, to,
                findPieceBySquare(from), findPieceBySquare(to), targetPiece);
    }

    private void checkCanMove(final Square from, final Square to,
                              final Piece movePiece, final Piece targetPiece) {
        if (!movePiece.movable(from, to, getMoveType(movePiece, targetPiece))) {
            throw new IllegalArgumentException("해당 칸으로 이동할 수 없습니다.");
        }
    }

    private MoveType getMoveType(final Piece sourcePiece, final Piece targetPiece) {
        if (targetPiece.isAlly(sourcePiece)) {
            throw new IllegalArgumentException("동료를 공격할 수 없습니다.");
        }
        return MoveType.of(sourcePiece.isEnemy(targetPiece));
    }

    private void checkHasPieceInRoute(final Square sourceSquare, final Square targetSquare) {
        Square tempSquare = sourceSquare;
        Direction direction = sourceSquare.findDirection(targetSquare);
        while (tempSquare.isDifferent(targetSquare)) {
            tempSquare = tempSquare.move(direction);
            checkHasPieceInSquare(targetSquare, tempSquare);
        }
    }

    private void checkHasPieceInSquare(final Square targetSquare, final Square tempSquare) {
        if (findPieceBySquare(tempSquare).isPlayerPiece() && tempSquare.isDifferent(targetSquare)) {
            throw new IllegalArgumentException("경로 중 기물이 존재하여 이동할 수 없습니다.");
        }
    }

    private void updateBoard(final Square sourceSquare, final Square targetSquare, final Piece sourcePiece) {
        board.replace(targetSquare, sourcePiece);
        board.replace(sourceSquare, new Empty());
    }

    public Score calculateScore() {
        return Score.of(board);
    }

    public GameResult getResult() {
        return GameResult.from(board, calculateScore());
    }

    public Map<Square, Piece> getValue() {
        return Map.copyOf(board);
    }
}
