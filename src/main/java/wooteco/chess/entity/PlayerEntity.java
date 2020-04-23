package wooteco.chess.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import wooteco.chess.domain.player.Player;
import wooteco.chess.domain.player.Record;
import wooteco.chess.domain.player.Result;

@Table("player")
public class PlayerEntity {
    @Id private int id;
    private String username;
    private String password;
    private int win;
    private int lose;
    private int draw;

    public PlayerEntity(Player player) {
        this.id = player.getId();
        this.username = player.getUsername();
        this.password = player.getPassword();
        this.win = player.recordOf(Result.WIN);
        this.lose = player.recordOf(Result.LOSE);
        this.draw = player.recordOf(Result.DRAW);
    }

    public int getId() {
        return id;
    }

    public Player toModel() {
        return new Player(id, username, password, Record.of(win, lose, draw));
    }

    private PlayerEntity() {
    }
}
