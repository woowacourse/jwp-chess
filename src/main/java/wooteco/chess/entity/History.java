package wooteco.chess.entity;

import org.springframework.data.annotation.Id;

public class History {
    @Id
    private Long id;
    private String start;
    private String end;

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
