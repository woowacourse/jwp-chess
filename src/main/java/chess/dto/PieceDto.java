package chess.dto;

import java.util.Objects;

public class PieceDto {

    private final String position;
    private final String team;
    private final String name;

    public PieceDto(final String position, final String team, final String name) {
        this.position = position;
        this.team = team;
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public String getTeam() {
        return team;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PieceDto pieceDto = (PieceDto) o;
        return Objects.equals(team, pieceDto.team) && Objects.equals(name, pieceDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, name);
    }

    @Override
    public String toString() {
        return team + ", " + name;
    }
}
