package chess.entity;

import java.util.Objects;

public class GameEntity {

    private final int id;
    private final String name;
    private final boolean running;

    public GameEntity(int id, String name, boolean running) {
        this.id = id;
        this.name = name;
        this.running = running;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameEntity that = (GameEntity) o;
        return id == that.id
                && running == that.running
                && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, running);
    }

    @Override
    public String toString() {
        return "GameEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", running=" + running +
                '}';
    }
}
