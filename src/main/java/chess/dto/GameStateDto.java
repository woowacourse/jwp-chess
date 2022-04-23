package chess.dto;

import chess.domain.game.statistics.GameState;
import java.util.Objects;

public class GameStateDto {

    private final int id;
    private final GameState state;

    public GameStateDto(int id, GameState state) {
        this.id = id;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public GameState getState() {
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameStateDto that = (GameStateDto) o;
        return id == that.id
                && state == that.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, state);
    }

    @Override
    public String toString() {
        return "GameStateDto{" + "id=" + id + ", state=" + state + '}';
    }
}