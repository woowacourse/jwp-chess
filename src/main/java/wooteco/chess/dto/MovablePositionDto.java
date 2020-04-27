package wooteco.chess.dto;

import java.util.List;

public class MovablePositionDto {
    private final List<String> positions;

    public MovablePositionDto(final List<String> positions) {
        this.positions = positions;
    }

    public List<String> getPositions() {
        return positions;
    }
}
