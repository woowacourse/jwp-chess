package wooteco.chess.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import wooteco.chess.domain.Game;
import wooteco.chess.domain.piece.Side;
import wooteco.chess.domain.player.Player;

@Table("game")
public class GameEntity {
    @Id private String id;
    private String title;
    @Column("white") private int whiteId;
    @Column("black") private int blackId;

    public GameEntity(Game game) {
        this.id = game.getId();
        this.title = game.getTitle();
        this.whiteId = game.getPlayerId(Side.WHITE);
        this.blackId = game.getPlayerId(Side.BLACK);
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public int getWhiteId() {
        return whiteId;
    }

    public int getBlackId() {
        return blackId;
    }

    public String getTitle() {
        return title;
    }

    public Game toModel(Player white, Player black) {
        return new Game(id, title, white, black);
    }

    private GameEntity() {
    }
}
