package wooteco.chess.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("move")
public class MoveEntity {
    @Id private int id;
    @Column("start_position") private String start;
    @Column("end_position") private String end;
    @Column("game") private String gameId;

    public MoveEntity(final String gameId, final String start, final String end) {
        this.gameId = gameId;
        this.start = start;
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    private MoveEntity() {
    }
}
