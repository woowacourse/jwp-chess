package wooteco.chess.domain.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardEntity that = (BoardEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(roomId, that.roomId) &&
                Objects.equals(position, that.position) &&
                Objects.equals(piece, that.piece);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roomId, position, piece);
    }
}