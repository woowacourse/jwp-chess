package wooteco.chess.dto;

public class MoveRequestDto {

    private Long id;
    private String sourcePosition;
    private String targetPosition;

    public MoveRequestDto(final Long id, final String sourcePosition, final String targetPosition) {
        this.id = id;
        this.sourcePosition = sourcePosition;
        this.targetPosition = targetPosition;
    }

    public Long getId() {
        return id;
    }

    public String getSourcePosition() {
        return sourcePosition;
    }

    public String getTargetPosition() {
        return targetPosition;
    }
}
