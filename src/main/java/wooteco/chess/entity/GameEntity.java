package wooteco.chess.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import wooteco.chess.domain.Game;
import wooteco.chess.domain.piece.Side;
import wooteco.chess.domain.player.Player;

@Table("game")
public class GameEntity {
    @Id private int id;
    @Column("white") private int whiteId;
    @Column("black") private int blackId;

    public GameEntity(Game game) {
        this.whiteId = game.getPlayerId(Side.WHITE);
        this.blackId = game.getPlayerId(Side.BLACK);
    }

    public int getId() {
        return id;
    }

    public int getWhiteId() {
        return whiteId;
    }

    public int getBlackId() {
        return blackId;
    }

    public Game toModel(Player white, Player black) {
        return new Game(id, white, black);
    }

    private GameEntity() {
    }
}
