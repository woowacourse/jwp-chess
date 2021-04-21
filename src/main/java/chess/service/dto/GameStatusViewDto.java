package chess.service.dto;

public class GameStatusViewDto {

    private String chessName;
    private String message;

    public GameStatusViewDto() {
    }

    public GameStatusViewDto(final String chessName, final boolean isRunning) {
        this.chessName = chessName;

        if (isRunning) {
            this.message = "O";
            return;
        }
        this.message = "X";
    }

    public GameStatusViewDto(final String chessName, final String message) {
        this.chessName = chessName;
        this.message = message;
    }

    public String getChessName() {
        return chessName;
    }

    public String getMessage() {
        return message;
    }

    public void setChessName(final String chessName) {
        this.chessName = chessName;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
