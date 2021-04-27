package chess.controller.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SaveRequestDto {
    @JsonProperty
    private final long gameId;
    @JsonProperty
    private final String pieces;

    public SaveRequestDto(long gameId, String pieces) {
        this.gameId = gameId;
        this.pieces = pieces;
    }

    public long getGameId() {
        return gameId;
    }

    public String getPieces() {
        return pieces;
    }
}
