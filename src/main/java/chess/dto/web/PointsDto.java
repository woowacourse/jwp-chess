package chess.dto.web;

import java.util.List;

public class PointsDto {

    private final List<PointDto> points;

    public PointsDto(List<PointDto> points) {
        this.points = points;
    }

    public List<PointDto> getPoints() {
        return points;
    }
}
