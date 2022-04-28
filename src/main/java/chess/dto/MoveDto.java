package chess.dto;

public class MoveDto {

    private String source;
    private String destination;

    public MoveDto() {}

    public MoveDto(String source, String destination) {
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
