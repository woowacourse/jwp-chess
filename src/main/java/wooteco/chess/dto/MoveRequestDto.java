package wooteco.chess.dto;

import java.util.UUID;

public class MoveRequestDto {

    private UUID id;
    private String sourcePosition;
    private String targetPosition;

    public MoveRequestDto(final UUID id, final String sourcePosition, final String targetPosition) {
        this.id = id;
        this.sourcePosition = sourcePosition;
        this.targetPosition = targetPosition;
    }

    public UUID getId() {
        return id;
    }

    public String getSourcePosition() {
        return sourcePosition;
    }

    public String getTargetPosition() {
        return targetPosition;
    }
}
