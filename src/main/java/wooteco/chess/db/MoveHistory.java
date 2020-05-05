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

    MoveHistory() {
    }

    public MoveHistory(String team, String sourcePosition, String targetPosition) {
        this.team = team;
        this.sourcePosition = sourcePosition;
        this.targetPosition = targetPosition;
    }

    public Long getId() {
        return id;
    }

    public Integer getMoves() {
        return moves;
    }

    public String getTeam() {
        return team;
    }

    public String getSourcePosition() {
        return sourcePosition;
    }

    public String getTargetPosition() {
        return targetPosition;
    }
}
