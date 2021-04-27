package chess.domain.entity;

import java.time.LocalDateTime;

public class History {
    private int id;
    private int roomId;
    private String startPosition;
    private String endPosition;
    private LocalDateTime registerDate;

    public History(final String startPosition, final String endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public String getStartPosition() {
        return startPosition;
    }

    public String getEndPosition() {
        return endPosition;
    }
}
