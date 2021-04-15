package chess.domain.piece;

import static chess.domain.piece.Color.BLACK;
import static chess.domain.piece.Color.WHITE;

import java.util.Arrays;

public enum PieceType {
    ROOK("r") {
        @Override
        public Piece of(String name) {
            if (name.equals(this.pieceName.toUpperCase())) {
                return new Rook(BLACK);
            }
            if (name.equals(this.pieceName)) {
                return new Rook(WHITE);
            }
            return new EmptyPiece();
        }
    },
    KNIGHT("n") {
        @Override
        public Piece of(String name) {
            if (name.equals(this.pieceName.toUpperCase())) {
                return new Knight(BLACK);
            }
            if (name.equals(this.pieceName)) {
                return new Knight(WHITE);
            }
            return new EmptyPiece();
        }
    },
    BISHOP("b") {
        @Override
        public Piece of(String name) {
            if (name.equals(this.pieceName.toUpperCase())) {
                return new Bishop(BLACK);
            }
            if (name.equals(this.pieceName)) {
                return new Bishop(WHITE);
            }
            return new EmptyPiece();
        }
    },
    QUEEN("q") {
        @Override
        public Piece of(String name) {
            if (name.equals(this.pieceName.toUpperCase())) {
                return new Queen(BLACK);
            }
            if (name.equals(this.pieceName)) {
                return new Queen(WHITE);
            }
            return new EmptyPiece();
        }
    },
    KING("k") {
        @Override
        public Piece of(String name) {
            if (name.equals(this.pieceName.toUpperCase())) {
                return new King(BLACK);
            }
            if (name.equals(this.pieceName)) {
                return new King(WHITE);
            }
            return new EmptyPiece();
        }
    },
    PAWN("p") {
        @Override
        public Piece of(String name) {
            if (name.equals(this.pieceName.toUpperCase())) {
                return new Pawn(BLACK);
            }
            if (name.equals(this.pieceName)) {
                return new Pawn(WHITE);
            }
            return new EmptyPiece();
        }
    };
    protected final String pieceName;
    
    PieceType(String pieceName) {
        this.pieceName = pieceName;
    }
    
    public static Piece findPiece(String name) {
        return Arrays.stream(PieceType.values())
            .filter(piece -> piece.pieceName.equals(name) || piece.pieceName.equals(name.toLowerCase()))
            .map(pieceType -> pieceType.of(name))
            .findAny()
            .orElseGet(EmptyPiece::new);
    }

    public abstract Piece of(String name);
}