package wooteco.chess.entity;

import org.springframework.data.relational.core.mapping.Table;

@Table("cell")
public class Cell {

    private String position;
    private String piece;

    public Cell(String position, String piece) {
        this.position = position;
        this.piece = piece;
    }

    public String getPosition() {
        return position;
    }

    public String getPiece() {
        return piece;
    }
}
