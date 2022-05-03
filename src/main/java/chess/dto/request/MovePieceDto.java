package chess.dto.request;

public class MovePieceDto {

    private String source;
    private String destination;

    public MovePieceDto() {
    }

    public MovePieceDto(String source, String destination) {
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
