package wooteco.chess.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("gameinfo")
public class GameInfoEntity {

    @Id
    private Long id;

    @Column
    private Long black;

    @Column
    private Long white;

    @Column
    private int turn;

    public GameInfoEntity(Long black, Long white, int turn) {
        this.black = black;
        this.white = white;
        this.turn = turn;
    }

    public Long getId() {
        return id;
    }

    public Long getBlack() {
        return black;
    }

    public Long getWhite() {
        return white;
    }

    public int getTurn() {
        return turn;
    }
}
