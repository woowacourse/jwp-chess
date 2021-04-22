package chess.controller.dto;

import java.util.List;

public class ReachablePositionsDto {
    private final List<String> positions;

    public ReachablePositionsDto(List<String> positions) {
        this.positions = positions;
    }

    public List<String> getPositions() {
        return positions;
    }
}
