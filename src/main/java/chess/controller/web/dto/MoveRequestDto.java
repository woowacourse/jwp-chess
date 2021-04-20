package chess.controller.web.dto;

import java.beans.ConstructorProperties;
import java.util.Objects;

public class MoveRequestDto {
    private final long gameId;
    private final String from;
    private final String to;

    @ConstructorProperties({"gameId", "from", "to"})
    public MoveRequestDto(long gameId, String from, String to) {
        this.gameId = gameId;
        this.from = from;
        this.to = to;
    }

    public long getGameId() {
        return gameId;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "MoveRequestDto{" +
                "id=" + gameId +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MoveRequestDto)) return false;
        MoveRequestDto that = (MoveRequestDto) o;
        return getGameId() == that.getGameId() && Objects.equals(from, that.from) && Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGameId(), from, to);
    }
}
