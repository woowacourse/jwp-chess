package wooteco.chess.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("board_status")
public class ChessPiece {
    @Id
    private String gameId;
    private String position;
    private String piece;

    public ChessPiece(String gameId, String position, String piece) {
        this.gameId = gameId;
        this.position = position;
        this.piece = piece;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
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
