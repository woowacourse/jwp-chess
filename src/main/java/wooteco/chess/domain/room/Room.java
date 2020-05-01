package wooteco.chess.domain.room;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import wooteco.chess.domain.piece.Team;


@Table("room")
public class Room {
    @Id
    private Long id;
    private String title;
    private String turn;

    public Room(final String title) {
        this.title = title;
        this.turn = Team.WHITE.name();
    }

    public boolean isWhiteTurn(){
        return turn.equals(Team.WHITE.name());
    }

    public void setTurn(Team turn) {
        this.turn = turn.name();
    }

    public String getTurn() {
        return turn;
    }

    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
    }
}
