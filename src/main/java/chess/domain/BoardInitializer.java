package chess.domain;

import static chess.domain.piece.detail.Team.BLACK;
import static chess.domain.piece.detail.Team.NONE;
import static chess.domain.piece.detail.Team.WHITE;

import java.util.HashMap;
import java.util.Map;

import chess.domain.piece.Blank;
import chess.domain.piece.Piece;
import chess.domain.piece.multiplemove.Bishop;
import chess.domain.piece.multiplemove.Queen;
import chess.domain.piece.multiplemove.Rook;
import chess.domain.piece.pawn.Pawn;
import chess.domain.piece.singlemove.King;
import chess.domain.piece.singlemove.Knight;
import chess.domain.square.File;
import chess.domain.square.Rank;
import chess.domain.square.Square;

public class BoardInitializer {

    public static Map<Square, Piece> create() {
        final HashMap<Square, Piece> board = new HashMap<>();
        createKings(board);
        createQueens(board);
        createBishops(board);
        createKnights(board);
        createRooks(board);
        createPawns(board);
        createBlanks(board);

        return board;
    }

    private static void createKings(final Map<Square, Piece> board) {
        board.put(Square.from("e1"), new King(WHITE, Square.from("e1")));
        board.put(Square.from("e8"), new King(BLACK, Square.from("e8")));
    }

    private static void createQueens(final Map<Square, Piece> board) {
        board.put(Square.from("d1"), new Queen(WHITE, Square.from("d1")));
        board.put(Square.from("d8"), new Queen(BLACK, Square.from("d8")));
    }

    private static void createBishops(final Map<Square, Piece> board) {
        board.put(Square.from("c1"), new Bishop(WHITE, Square.from("c1")));
        board.put(Square.from("f1"), new Bishop(WHITE, Square.from("f1")));
        board.put(Square.from("c8"), new Bishop(BLACK, Square.from("c8")));
        board.put(Square.from("f8"), new Bishop(BLACK, Square.from("f8")));
    }

    private static void createKnights(final Map<Square, Piece> board) {
        board.put(Square.from("b1"), new Knight(WHITE, Square.from("b1")));
        board.put(Square.from("g1"), new Knight(WHITE, Square.from("g1")));
        board.put(Square.from("b8"), new Knight(BLACK, Square.from("b8")));
        board.put(Square.from("g8"), new Knight(BLACK, Square.from("g8")));
    }

    private static void createRooks(final Map<Square, Piece> board) {
        board.put(Square.from("a1"), new Rook(WHITE, Square.from("a1")));
        board.put(Square.from("h1"), new Rook(WHITE, Square.from("h1")));
        board.put(Square.from("a8"), new Rook(BLACK, Square.from("a8")));
        board.put(Square.from("h8"), new Rook(BLACK, Square.from("h8")));
    }

    private static void createPawns(final Map<Square, Piece> board) {
        for (final File file : File.values()) {
            board.put(Square.of(file, Rank.from(2)), Pawn.of(WHITE, Square.of(file, Rank.from(2))));
            board.put(Square.of(file, Rank.from(7)), Pawn.of(BLACK, Square.of(file, Rank.from(7))));
        }
    }

    private static void createBlanks(final HashMap<Square, Piece> board) {
        for (final File file : File.values()) {
            board.put(Square.of(file, Rank.from(3)), new Blank(NONE, Square.of(file, Rank.from(3))));
            board.put(Square.of(file, Rank.from(4)), new Blank(NONE, Square.of(file, Rank.from(4))));
            board.put(Square.of(file, Rank.from(5)), new Blank(NONE, Square.of(file, Rank.from(5))));
            board.put(Square.of(file, Rank.from(6)), new Blank(NONE, Square.of(file, Rank.from(6))));
        }
    }
}
