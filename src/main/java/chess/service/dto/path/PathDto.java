package chess.service.dto.path;

import chess.domain.board.position.Path;
import chess.domain.board.position.Position;

import java.util.List;
import java.util.stream.Collectors;

public class PathDto {

    private List<String> path;

    public PathDto() {
    }

    private PathDto(final List<String> path) {
        this.path = path;
    }

    public static PathDto from(final Path path) {
        return new PathDto(path.stream()
                .map(Position::parseString)
                .collect(Collectors.toList()));
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(final List<String> path) {
        this.path = path;
    }
}
