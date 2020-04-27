package wooteco.chess.dto;

public class MoveRequestDto {

    private Integer roomId;
    private String sourcePosition;
    private String targetPosition;

    public MoveRequestDto(final Integer roomId, final String sourcePosition, final String targetPosition) {
        this.roomId = roomId;
        this.sourcePosition = sourcePosition;
        this.targetPosition = targetPosition;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public String getSourcePosition() {
        return sourcePosition;
    }

    public String getTargetPosition() {
        return targetPosition;
    }
}
