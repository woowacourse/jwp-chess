package wooteco.chess.entity;

import org.springframework.data.annotation.Id;

public class History {
    @Id
    private Long id;
    private String start;
    private String end;
    private GameRef game;

    public History() {
    }

    public History(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public History(String start, String end, Game game) {
        this.start = start;
        this.end = end;
        this.game = new GameRef(game);
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
