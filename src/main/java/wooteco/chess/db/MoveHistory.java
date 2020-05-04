package wooteco.chess.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("move_history")
public class MoveHistory {
    @Id
    private Long id;
    private Integer moves;
    private String team;
    private String sourcePosition;
    private String targetPosition;

    public MoveHistory() {
    }

    public MoveHistory(String team, String sourcePosition, String targetPosition) {
        this.team = team;
        this.sourcePosition = sourcePosition;
        this.targetPosition = targetPosition;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMoves() {
        return moves;
    }

    public void setMoves(Integer moves) {
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
