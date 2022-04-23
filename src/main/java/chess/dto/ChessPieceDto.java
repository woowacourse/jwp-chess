package chess.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChessPieceDto {

    private final String position;
    private final String pieceType;
    private final String color;

    private ChessPieceDto(final String position, final String pieceType, final String color) {
        this.position = position;
        this.pieceType = pieceType;
        this.color = color;
    }

    public static ChessPieceDto from(final ResultSet resultSet) throws SQLException {
        return new ChessPieceDto(
                resultSet.getString("position"),
                resultSet.getString("chess_piece"),
                resultSet.getString("color")
        );
    }

    public String getPosition() {
        return position;
    }

    public String getPieceType() {
        return pieceType;
    }

    public String getColor() {
        return color;
    }
}
