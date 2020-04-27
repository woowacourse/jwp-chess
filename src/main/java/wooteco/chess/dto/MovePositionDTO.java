package wooteco.chess.dto;

public class MovePositionDTO {
    private final String sourcePosition;
    private final String targetPosition;

    public MovePositionDTO(final String sourcePosition, final String targetPosition) {
        this.sourcePosition = sourcePosition;
        this.targetPosition = targetPosition;
    }

    public String getSourcePosition() {
        return sourcePosition;
    }

    public String getTargetPosition() {
        return targetPosition;
    }
}
