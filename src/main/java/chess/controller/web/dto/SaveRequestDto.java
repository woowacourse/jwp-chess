package chess.controller.web.dto;

public class SaveRequestDto {
    private final long gameId;
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
