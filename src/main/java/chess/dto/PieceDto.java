package chess.dto;

import chess.domain.Camp;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.domain.piece.Type;

public class PieceDto {
    private final String type;
    private final String camp;
    private final String position;

    public PieceDto(String type, String camp, String position) {
        this.type = type;
        this.camp = camp;
        this.position = position;
    }

    public static PieceDto of(String type, boolean isWhite, String position) {
        if (isWhite) {
            return new PieceDto(type, "white", position);
        }
        return new PieceDto(type, "black", position);
    }

    public static PieceDto of(Piece piece, Position position) {
        return of(piece.getType().toString(), piece.isCamp(Camp.WHITE), position.toString());
    }

    public Piece getPieceAsObject() {
        String rawType = this.type;
        if (rawType.isBlank()) {
            rawType = "none";
        }
        Type type = Type.valueOf(rawType.toUpperCase());
        Camp camp = Camp.valueOf(this.camp.toUpperCase());
        return type.generatePiece(camp);
    }

    public String getType() {
        return type;
    }

    public String getCamp() {
        return camp;
    }

    public String getPosition() {
        return position;
    }
}
