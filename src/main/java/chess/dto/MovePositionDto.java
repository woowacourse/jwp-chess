package chess.dto;

public class MovePositionDto {

    private final String roomName;
    private final String current;
    private final String destination;

    public MovePositionDto(String chessGameName, String current, String destination) {
        this.roomName = chessGameName;
        this.current = current;
        this.destination = destination;
    }

    public String getCurrent() {
        return current;
    }

    public String getDestination() {
        return destination;
    }

    public String getRoomName() {
        return roomName;
    }
}
