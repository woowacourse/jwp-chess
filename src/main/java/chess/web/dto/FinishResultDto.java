package chess.web.dto;

public class FinishResultDto {
    private int roomId;
    private boolean finished;

    public FinishResultDto() {
    }

    public FinishResultDto(int roomId, boolean finished) {
        this.roomId = roomId;
        this.finished = finished;
    }

    public int getRoomId() {
        return roomId;
    }

    public boolean isFinished() {
        return finished;
    }
}
