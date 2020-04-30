package wooteco.chess.dto;

public class MoveRequestDto {

    private Long roomId;
    private String sourcePosition;
    private String targetPosition;

    public MoveRequestDto(final Long roomId, final String sourcePosition, final String targetPosition) {
        this.roomId = roomId;
        this.sourcePosition = sourcePosition;
        this.targetPosition = targetPosition;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getSourcePosition() {
        return sourcePosition;
    }

    public String getTargetPosition() {
        return targetPosition;
    }
}
