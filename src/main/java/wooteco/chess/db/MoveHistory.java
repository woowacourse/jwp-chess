package wooteco.chess.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("move_history")
public class MoveHistory {
    @Id
    private String gameId;
    private int moves;
    private String team;
    private String sourcePosition;
    private String targetPosition;

    public MoveHistory(String gameId, int moves, String team, String sourcePosition, String targetPosition) {
        this.gameId = gameId;
        this.moves = moves;
        this.team = team;
        this.sourcePosition = sourcePosition;
        this.targetPosition = targetPosition;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getSourcePosition() {
        return sourcePosition;
    }

    public void setSourcePosition(String sourcePosition) {
        this.sourcePosition = sourcePosition;
    }

    public String getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(String targetPosition) {
        this.targetPosition = targetPosition;
    }
}
