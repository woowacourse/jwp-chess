package chess.dto.web;

public class MovementDto {

    private String source;
    private String destination;

    public MovementDto() {

    }

    public MovementDto(String source, String destination) {
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
