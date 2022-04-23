package chess.web.dto;

public class MoveResultDto {

    private final int status;
    private final String errorMessage;
    private final boolean isGameOver;

    public MoveResultDto(int status, String errorMessage, boolean isGameOver) {
        this.status = status;
        this.errorMessage = errorMessage;
        this.isGameOver = isGameOver;
    }

    public int getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isGameOver() {
        return isGameOver;
    }
}
