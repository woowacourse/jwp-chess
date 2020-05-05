package wooteco.chess.domain.board;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceType;
import wooteco.chess.domain.position.Position;

import java.util.HashMap;
import java.util.Map;

public class BoardFactory {
    private static final int BLANK_START_ROW_INDEX = 3;
    private static final int BLANK_END_ROW_INDEX = 6;

    private static final Map<Position, Piece> BOARD = new HashMap<>();
    private static final int ASCII_GAP = 96;

    static {
        setUpPieces();
    }

    private static void setUpPieces() {
        BOARD.put(Position.of("a1"), Piece.of(PieceType.WHITE_ROOK));
        BOARD.put(Position.of("b1"), Piece.of(PieceType.WHITE_KNIGHT));
        BOARD.put(Position.of("c1"), Piece.of(PieceType.WHITE_BISHOP));
        BOARD.put(Position.of("d1"), Piece.of(PieceType.WHITE_QUEEN));
        BOARD.put(Position.of("e1"), Piece.of(PieceType.WHITE_KING));
        BOARD.put(Position.of("f1"), Piece.of(PieceType.WHITE_BISHOP));
        BOARD.put(Position.of("g1"), Piece.of(PieceType.WHITE_KNIGHT));
        BOARD.put(Position.of("h1"), Piece.of(PieceType.WHITE_ROOK));

        BOARD.put(Position.of("a2"), Piece.of(PieceType.FIRST_WHITE_PAWN));
        BOARD.put(Position.of("b2"), Piece.of(PieceType.FIRST_WHITE_PAWN));
        BOARD.put(Position.of("c2"), Piece.of(PieceType.FIRST_WHITE_PAWN));
        BOARD.put(Position.of("d2"), Piece.of(PieceType.FIRST_WHITE_PAWN));
        BOARD.put(Position.of("e2"), Piece.of(PieceType.FIRST_WHITE_PAWN));
        BOARD.put(Position.of("f2"), Piece.of(PieceType.FIRST_WHITE_PAWN));
        BOARD.put(Position.of("g2"), Piece.of(PieceType.FIRST_WHITE_PAWN));
        BOARD.put(Position.of("h2"), Piece.of(PieceType.FIRST_WHITE_PAWN));

        for (int row = BLANK_START_ROW_INDEX; row <= BLANK_END_ROW_INDEX; row++) {
            for (int col = Position.START_INDEX; col <= Position.END_INDEX; col++) {
                String position = (char) (col + ASCII_GAP) + String.valueOf(row);
                BOARD.put(Position.of(position), Piece.of(PieceType.BLANK));
            }
        }

        BOARD.put(Position.of("a7"), Piece.of(PieceType.FIRST_BLACK_PAWN));
        BOARD.put(Position.of("b7"), Piece.of(PieceType.FIRST_BLACK_PAWN));
        BOARD.put(Position.of("c7"), Piece.of(PieceType.FIRST_BLACK_PAWN));
        BOARD.put(Position.of("d7"), Piece.of(PieceType.FIRST_BLACK_PAWN));
        BOARD.put(Position.of("e7"), Piece.of(PieceType.FIRST_BLACK_PAWN));
        BOARD.put(Position.of("f7"), Piece.of(PieceType.FIRST_BLACK_PAWN));
        BOARD.put(Position.of("g7"), Piece.of(PieceType.FIRST_BLACK_PAWN));
        BOARD.put(Position.of("h7"), Piece.of(PieceType.FIRST_BLACK_PAWN));

        BOARD.put(Position.of("a8"), Piece.of(PieceType.BLACK_ROOK));
        BOARD.put(Position.of("b8"), Piece.of(PieceType.BLACK_KNIGHT));
        BOARD.put(Position.of("c8"), Piece.of(PieceType.BLACK_BISHOP));
        BOARD.put(Position.of("d8"), Piece.of(PieceType.BLACK_QUEEN));
        BOARD.put(Position.of("e8"), Piece.of(PieceType.BLACK_KING));
        BOARD.put(Position.of("f8"), Piece.of(PieceType.BLACK_BISHOP));
        BOARD.put(Position.of("g8"), Piece.of(PieceType.BLACK_KNIGHT));
        BOARD.put(Position.of("h8"), Piece.of(PieceType.BLACK_ROOK));
    }

    private BoardFactory() {
    }

    public static Board initializeBoard() {
        return new Board(BOARD);
    }
}