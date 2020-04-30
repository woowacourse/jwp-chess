package wooteco.chess.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("chess")
public class ChessEntity {
    @Id
    private Long roomId;
    private String board;
    private Boolean isWhite;

    public ChessEntity() {
    }

    public ChessEntity(String board, boolean isWhite) {
        this.board = board;
        this.isWhite = isWhite;
    }

    public ChessEntity(Long roomId, String board, boolean isWhite) {
        System.out.println("엔티티 생성자");
        this.roomId = roomId;
        this.board = board;
        this.isWhite = isWhite;
    }

    public ChessEntity(Long roomId, String board) {
        this(roomId, board, true);
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getBoard() {
        return board;
    }

    public boolean getIsWhite() {
        System.out.println("디비->불리언");
        return isWhite;
    }
}
