package chess.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

class MoveRequestDto {
    @JsonProperty
    private long gameId;
    @JsonProperty
    private long roomId;
    @JsonProperty
    private String from;
    @JsonProperty
    private String to;

    public MoveRequestDto() {
    }

    public MoveRequestDto(long gameId, long roomId, String from, String to) {
        this.gameId = gameId;
        this.roomId = roomId;
        this.from = from;
        this.to = to;
    }

    public long getGameId() {
        return gameId;
    }

    public long getRoomId() {
        return roomId;
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
