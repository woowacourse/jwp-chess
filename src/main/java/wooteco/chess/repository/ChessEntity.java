package wooteco.chess.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table("chess")
public class ChessEntity {
    @Id
    private Long roomId;
    private String title;
    private String board;
    private Boolean isWhite;

    public ChessEntity() {
    }

    public ChessEntity(Long roomId, String title, String board) {
        this.roomId = roomId;
        this.title = title;
        this.board = board;
        this.isWhite = true;
    }

    public ChessEntity(String title, String board, boolean isWhite) {
        this.title = title;
        this.board = board;
        this.isWhite = isWhite;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getTitle() {
        return title;
    }

    public String getBoard() {
        return board;
    }

    public boolean getIsWhite() {
        return isWhite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessEntity entity = (ChessEntity) o;
        return Objects.equals(roomId, entity.roomId) &&
                Objects.equals(title, entity.title) &&
                Objects.equals(board, entity.board) &&
                Objects.equals(isWhite, entity.isWhite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId, title, board, isWhite);
    }
}
