package chess.dto;

import java.util.Objects;

public class GameOverviewDto {

    private final int id;
    private final String name;

    public GameOverviewDto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameOverviewDto that = (GameOverviewDto) o;
        return id == that.id
                && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "GameOverviewDto{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
