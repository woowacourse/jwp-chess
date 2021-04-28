package chess.webdto.view;

public class MoveRequestDto {
    private String start;
    private String destination;

    public MoveRequestDto() {
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
