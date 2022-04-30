package chess.dto;

public class StatusDto {

    private final boolean isRunning;

    public StatusDto(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
