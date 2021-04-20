package chess.dto.result;

public final class ResultDTO {
    private String roomId;
    private String winner;
    private String loser;

    public ResultDTO() {
    }

    public ResultDTO(final String roomId, final String winner, final String loser) {
        this.roomId = roomId;
        this.winner = winner;
        this.loser = loser;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getWinner() {
        return winner;
    }

    public String getLoser() {
        return loser;
    }
}