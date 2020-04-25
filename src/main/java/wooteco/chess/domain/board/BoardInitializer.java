package wooteco.chess.domain.board;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceType;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.position.Positions;

import java.util.HashMap;
import java.util.Map;

public class BoardInitializer {
    private static Map<Position, Piece> board = new HashMap<>();

    public static Map<Position, Piece> initializeAll() {
        initializeBishop();
        initializeKing();
        initializeKnight();
        initializePawn();
        initializeQueen();
        initializeRook();

        return board;
    }

    private static void initializeRook() {
        board.put(Positions.of("a8"), new Piece(PieceType.ROOK, Team.BLACK));
        board.put(Positions.of("a1"), new Piece(PieceType.ROOK, Team.WHITE));
        board.put(Positions.of("h8"), new Piece(PieceType.ROOK, Team.BLACK));
        board.put(Positions.of("h1"), new Piece(PieceType.ROOK, Team.WHITE));
    }

    private static void initializeQueen() {
        board.put(Positions.of("d8"), new Piece(PieceType.QUEEN, Team.BLACK));
        board.put(Positions.of("d1"), new Piece(PieceType.QUEEN, Team.WHITE));
    }

    private static void initializePawn() {
        board.put(Positions.of("a7"), new Piece(PieceType.PAWN, Team.BLACK));
        board.put(Positions.of("b7"), new Piece(PieceType.PAWN, Team.BLACK));
        board.put(Positions.of("c7"), new Piece(PieceType.PAWN, Team.BLACK));
        board.put(Positions.of("d7"), new Piece(PieceType.PAWN, Team.BLACK));
        board.put(Positions.of("e7"), new Piece(PieceType.PAWN, Team.BLACK));
        board.put(Positions.of("f7"), new Piece(PieceType.PAWN, Team.BLACK));
        board.put(Positions.of("g7"), new Piece(PieceType.PAWN, Team.BLACK));
        board.put(Positions.of("h7"), new Piece(PieceType.PAWN, Team.BLACK));
        board.put(Positions.of("a2"), new Piece(PieceType.PAWN, Team.WHITE));
        board.put(Positions.of("b2"), new Piece(PieceType.PAWN, Team.WHITE));
        board.put(Positions.of("c2"), new Piece(PieceType.PAWN, Team.WHITE));
        board.put(Positions.of("d2"), new Piece(PieceType.PAWN, Team.WHITE));
        board.put(Positions.of("e2"), new Piece(PieceType.PAWN, Team.WHITE));
        board.put(Positions.of("f2"), new Piece(PieceType.PAWN, Team.WHITE));
        board.put(Positions.of("g2"), new Piece(PieceType.PAWN, Team.WHITE));
        board.put(Positions.of("h2"), new Piece(PieceType.PAWN, Team.WHITE));
    }

    private static void initializeKnight() {
        board.put(Positions.of("b8"), new Piece(PieceType.KNIGHT, Team.BLACK));
        board.put(Positions.of("b1"), new Piece(PieceType.KNIGHT, Team.WHITE));
        board.put(Positions.of("g8"), new Piece(PieceType.KNIGHT, Team.BLACK));
        board.put(Positions.of("g1"), new Piece(PieceType.KNIGHT, Team.WHITE));
    }

    private static void initializeKing() {
        board.put(Positions.of("e8"), new Piece(PieceType.KING, Team.BLACK));
        board.put(Positions.of("e1"), new Piece(PieceType.KING, Team.WHITE));
    }

    private static void initializeBishop() {
        board.put(Positions.of("c8"), new Piece(PieceType.BISHOP, Team.BLACK));
        board.put(Positions.of("c1"), new Piece(PieceType.BISHOP, Team.WHITE));
        board.put(Positions.of("f8"), new Piece(PieceType.BISHOP, Team.BLACK));
        board.put(Positions.of("f1"), new Piece(PieceType.BISHOP, Team.WHITE));
    }
}
