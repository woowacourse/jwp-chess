package chess.dto.request;

public class TurnChangeRequestDto {
    private final String currentTurn;
    private final String nextTurn;
    private final int roomId;

    public TurnChangeRequestDto(final String currentTurn, final String nextTurn, final int roomId) {
        this.currentTurn = currentTurn;
        this.nextTurn = nextTurn;
        this.roomId = roomId;
    }

    public String getCurrentTurn() {
        return currentTurn;
    }

    public String getNextTurn() {
        return nextTurn;
    }

    public int getRoomId() {
        return roomId;
    }
}
