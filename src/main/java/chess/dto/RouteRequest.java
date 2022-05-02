package chess.dto;

public class RouteRequest {

    private final String source;
    private final String destination;

    public RouteRequest() {
        this.source = null;
        this.destination = null;
    }

    public RouteRequest(String source, String destination) {
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
