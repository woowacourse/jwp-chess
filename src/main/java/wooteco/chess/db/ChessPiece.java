package wooteco.chess.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("board_status")
public class ChessPiece {
    @Id
    private Long id;
    private String position;
    private String piece;

    public ChessPiece() {
    }

    public ChessPiece(String position, String piece) {
        this.position = position;
        this.piece = piece;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPiece() {
        return piece;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }
}
