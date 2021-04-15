package chess.dto;

public class InitializeDTO {
    private String roomId;
    private String winner;
    private String loser;

    public InitializeDTO() {
    }

    public InitializeDTO(String roomId, String winner, String loser) {
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