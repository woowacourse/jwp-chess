package wooteco.chess.dto;

import java.util.List;

public class MovablePositionDTO {
    private final List<String> positions;

    public MovablePositionDTO(final List<String> positions) {
        this.positions = positions;
    }

    public List<String> getPositions() {
        return positions;
    }
}
