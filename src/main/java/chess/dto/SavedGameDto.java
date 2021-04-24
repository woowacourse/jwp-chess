package chess.dto;

public class SavedGameDto {
    private final ChessBoardDto chessBoardDto;
    private final String currentTurnColor;

    public SavedGameDto(ChessBoardDto chessBoardDto, String currentTurnColor) {
        this.chessBoardDto = chessBoardDto;
        this.currentTurnColor = currentTurnColor;
    }

    public ChessBoardDto getChessBoardDto() {
        return chessBoardDto;
    }

    public String getCurrentTurnColor() {
        return currentTurnColor;
    }
}
