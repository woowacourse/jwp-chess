package chess.dto.response;

public class MoveResponse {
    private final String source;
    private final String destination;

    public MoveResponse(String source, String destination) {
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
