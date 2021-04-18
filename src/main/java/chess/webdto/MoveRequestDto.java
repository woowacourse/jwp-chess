package chess.webdto;

public class MoveRequestDto {
    private final String start;
    private final String destination;

    public MoveRequestDto(String start, String destination) {
        this.start = start;
        this.destination = destination;
    }

    public String getStart() {
        return start;
    }

    public String getDestination() {
        return destination;
    }
}
