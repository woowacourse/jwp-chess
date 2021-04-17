package chess.dto;

import com.google.gson.JsonObject;

import java.util.Objects;

public class SparkRoomDTO {
    private final String name;
    private final String turn;
    private final JsonObject state;

    public SparkRoomDTO(String name, String turn, JsonObject state) {
        this.name = name;
        this.turn = turn;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public String getTurn() {
        return turn;
    }

    public JsonObject getState() {
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SparkRoomDTO sparkRoomDTO = (SparkRoomDTO) o;
        return Objects.equals(name, sparkRoomDTO.name) && Objects.equals(turn, sparkRoomDTO.turn) && Objects.equals(state, sparkRoomDTO.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, turn, state);
    }
}
