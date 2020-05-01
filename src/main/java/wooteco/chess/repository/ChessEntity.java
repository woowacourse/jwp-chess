package wooteco.chess.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("chess")
public class ChessEntity {
    @Id
    private Long roomId;
    private String title;
    private String board;
    private Boolean isWhite;

    public ChessEntity() {
    }

    public ChessEntity(String title, String board, boolean isWhite) {
        this.title = title;
        this.board = board;
        this.isWhite = isWhite;
    }

    public ChessEntity(Long roomId, String title, String board) {
        this.roomId = roomId;
        this.title = title;
        this.board = board;
        this.isWhite = true;
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
}
