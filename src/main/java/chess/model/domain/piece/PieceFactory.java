package chess.model.domain.piece;

import java.util.Arrays;

public enum PieceFactory {

    BLACK_PAWN(Pawn.getInstance(Team.BLACK)),
    WHITE_PAWN(Pawn.getInstance(Team.WHITE)),
    BLACK_ROOK(Rook.getInstance(Team.BLACK)),
    WHITE_ROOK(Rook.getInstance(Team.WHITE)),
    BLACK_KNIGHT(Knight.getInstance(Team.BLACK)),
    WHITE_KNIGHT(Knight.getInstance(Team.WHITE)),
    BLACK_BISHOP(Bishop.getInstance(Team.BLACK)),
    WHITE_BISHOP(Bishop.getInstance(Team.WHITE)),
    BLACK_QUEEN(Queen.getInstance(Team.BLACK)),
    WHITE_QUEEN(Queen.getInstance(Team.WHITE)),
    BLACK_KING(King.getInstance(Team.BLACK)),
    WHITE_KING(King.getInstance(Team.WHITE));

    private final Piece piece;

    PieceFactory(Piece piece) {
        this.piece = piece;
    }

    public static Piece getPiece(String pieceName) {
        return Arrays.stream(PieceFactory.values())
            .filter(PieceFactory -> PieceFactory.name().equalsIgnoreCase(pieceName))
            .map(pieceFactory -> pieceFactory.piece)
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    public static Piece getPiece(Team team, Type type) {
        return Arrays.stream(PieceFactory.values())
            .map(pieceFactory -> pieceFactory.piece)
            .filter(piece -> piece.isSameTeam(team))
            .filter(piece -> piece.isSameType(type))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    public static String getName(Piece piece) {
        return Arrays.stream(PieceFactory.values())
            .filter(pieceFactory -> pieceFactory.piece.equals(piece))
            .map(Enum::name)
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    public Piece getPiece() {
        return piece;
    }
}
