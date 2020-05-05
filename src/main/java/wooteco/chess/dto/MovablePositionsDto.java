package wooteco.chess.dto;

import java.util.Collections;
import java.util.List;

public class MovablePositionsDto {
    private final List<String> movablePositionNames;
    private final String position;

    public MovablePositionsDto(List<String> movablePositionNames, String position) {
        this.movablePositionNames = movablePositionNames;
        this.position = position;
    }

    public List<String> getMovablePositionNames() {
        return Collections.unmodifiableList(movablePositionNames);
    }

    public String getPosition() {
        return position;
    }
}
