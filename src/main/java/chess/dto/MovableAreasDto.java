package chess.dto;

import chess.model.domain.board.Square;
import java.util.Set;
import java.util.stream.Collectors;

public class MovableAreasDto {

    private Set<String> movableAreas;

    public MovableAreasDto(Set<Square> pathSquares) {
        movableAreas = pathSquares.stream()
            .map(Square::getName)
            .collect(Collectors.toSet());
    }

    public Set<String> getMovableAreas() {
        return movableAreas;
    }
}
