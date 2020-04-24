package chess.model.dto;

import chess.model.domain.board.Square;
import java.util.Set;
import java.util.stream.Collectors;

public class PathDto {

    private Set<String> path;

    public PathDto(Set<Square> pathSquares) {
        path = pathSquares.stream()
            .map(Square::getName)
            .collect(Collectors.toSet());
    }

    public Set<String> getPath() {
        return path;
    }
}
