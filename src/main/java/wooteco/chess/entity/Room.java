package wooteco.chess.entity;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("room")
public class Room {

    private static final int MAXIMUM_LENGTH = 10;

    @Id
    private Long id;
    private String name;

    @Column("room")
    private GameEntity game;

    public Room() {}

    public Room(String name) {
        validateName(name);
        this.name = name;
    }

    public Room(String name, GameEntity game) {
        this.name = name;
        this.game = game;
    }

    private void validateName(String name) {
        if (name.length() > MAXIMUM_LENGTH) {
            throw new IllegalArgumentException("이름은 최대 10자입니다.");
        }
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public GameEntity getGame() {
        return game;
    }

    public void updateGame(GameEntity game) {
        this.game = game;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Room room = (Room)o;
        return Objects.equals(name, room.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}