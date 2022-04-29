package chess.dto;

public class MoveDto {

    private String currentPosition;
    private String destinationPosition;

    private MoveDto() {
    }

    public MoveDto(String currentPosition, String destinationPosition) {
        this.currentPosition = currentPosition;
        this.destinationPosition = destinationPosition;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public String getDestinationPosition() {
        return destinationPosition;
    }
}
