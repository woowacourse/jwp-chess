package chess.dto.request;

import java.beans.ConstructorProperties;

public class MoveRequestDto {
    private final String source;
    private final String target;
    private final Long roomId;

    @ConstructorProperties({"source", "target", "roomId"})
    public MoveRequestDto(final String source, final String target, final Long roomId) {
        this.source = source;
        this.target = target;
        this.roomId = roomId;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public Long getRoomId() {
        return roomId;
    }
}
