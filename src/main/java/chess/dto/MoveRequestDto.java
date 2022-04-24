package chess.dto;

public class MoveRequestDto {
    private String piece;
    private String from;
    private String to;
    private String gameId;

    public MoveRequestDto(String piece, String from, String to, String gameId) {
        this.piece = piece;
        this.from = from;
        this.to = to;
        this.gameId = gameId;
    }

    public MoveRequestDto() {
    }

    public String getPiece() {
        return piece;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getGameId() {
        return gameId;
    }
}
