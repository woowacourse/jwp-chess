package chess.dto;

public class MoveRequestDto {
    private String target;
    private String destination;

    public MoveRequestDto() {

    }

    public MoveRequestDto(String target, String destination) {
        this.target = target;
        this.destination = destination;
    }

    public String getTarget() {
        return target;
    }

    public String getDestination() {
        return destination;
    }

}
