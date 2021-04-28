package chess.dto;

public class MoveInfoDto {
    private String target;
    private String destination;

    public MoveInfoDto() {
    }

    public MoveInfoDto(String target, String destination) {
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
