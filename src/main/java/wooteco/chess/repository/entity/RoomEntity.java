package wooteco.chess.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("room")
public class RoomEntity {

    @Id
    private Long id;
    private String name;
    private String password;
    @Column(value = "room")
    private GameEntity game;

    public RoomEntity(final String name, final String password, final GameEntity game) {
        this.name = name;
        this.password = password;
        this.game = game;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public GameEntity getGame() {
        return game;
    }

    public void updateGame(final GameEntity game) {
        this.game = game;
    }
}
