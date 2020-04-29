package wooteco.chess.domain.room;

import wooteco.chess.domain.piece.Team;

public class Room {
    private final Long id;

    private Team turn;
    private final String title;

    public Room(final Long id, final String title) {
        this.id = id;
        this.title = title;
        this.turn = Team.WHITE;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTurn() {
        return turn.name();
    }
}
