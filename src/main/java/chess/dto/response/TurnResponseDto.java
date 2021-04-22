package chess.dto.response;

public class TurnResponseDto {
    private final Long id;
    private final String currentTurn;

    public TurnResponseDto(final Long id, final String currentTurn) {
        this.id = id;
        this.currentTurn = currentTurn;
    }

    public String getCurrentTurn() {
        return currentTurn;
    }
}
