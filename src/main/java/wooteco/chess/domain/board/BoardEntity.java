package wooteco.chess.domain.board;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("board")
public class BoardEntity {
    @Id
    private Long id;
    @Column("roomId")
    private Long roomId;
    private String position;
    private String piece;

    public BoardEntity(final Long roomId, final String position, final String piece) {
        this.roomId = roomId;
        this.position = position;
        this.piece = piece;
    }

    public String getPosition() {
        return position;
    }

    public String getPiece() {
        return piece;
    }

    public void setPiece(final String piece) {
        this.piece = piece;
    }
}
