package chess.dto.web;

public class PointsDto {
    private final String source;
    private final String destination;

    public PointsDto(final String source, final String destination) {
        this.source = source;
        this.destination = destination;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }
}
