package chess.dto;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import java.util.Objects;

public final class BoardElementDto {

    private String pieceName;
    private String pieceColor;
    private String position;

    public BoardElementDto(Position position, Piece piece) {
        this.pieceName = piece.getNotation().name();
        this.pieceColor = piece.getColor().name();
        this.position = position.getFile().name() + position.getRankNumber();
    }

    public BoardElementDto(String pieceName, String pieceColor, String position) {
        this.pieceName = pieceName;
        this.pieceColor = pieceColor;
        this.position = position;
    }

    public String getPieceName() {
        return pieceName;
    }

    public String getPieceColor() {
        return pieceColor;
    }

    public String getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BoardElementDto that = (BoardElementDto) o;
        return Objects.equals(pieceName, that.pieceName) && Objects.equals(pieceColor, that.pieceColor)
                && Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceName, pieceColor, position);
    }
}
