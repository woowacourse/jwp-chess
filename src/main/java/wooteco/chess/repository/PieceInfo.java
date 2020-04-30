package wooteco.chess.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("board")
public class PieceInfo {

    @Id
    Long id;
    @Column
    private String piece;
    private String position;

    public PieceInfo(String piece, String position) {
        this.piece = piece;
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPiece() {
        return piece;
    }

    public String getPosition() {
        return position;
    }
}
