package chess.dto.web;

public class PointsDto {
    private String source;
    private String destination;

    public PointsDto(){}

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
