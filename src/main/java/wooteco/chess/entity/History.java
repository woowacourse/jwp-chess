package wooteco.chess.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("history")
public class History {
    private @Id Long id;
    private @Column("start") String start;
    private @Column("end") String end;

    public History() {
    }

    public History(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public Long getId() {
        return id;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }
}
