package chess.controller.web.dto.move;

import java.util.List;

public class PathResponseDto {

    private List<String> path;

    public PathResponseDto() {
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }
}
