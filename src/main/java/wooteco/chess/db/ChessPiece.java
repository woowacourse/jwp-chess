package wooteco.chess.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("board_status")
public class ChessPiece {
    @Id
    private Long id;
    private String position;
    private String piece;

    ChessPiece() {
    }

    public ChessPiece(String position, String piece) {
        this.position = position;
        this.piece = piece;
    }

    public Long getId() {
        return id;
    }

    public String getPosition() {
        return position;
    }

    public String getPiece() {
        return piece;
    }
}
