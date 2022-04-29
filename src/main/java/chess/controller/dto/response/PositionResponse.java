package chess.controller.dto.response;

public class PositionResponse {

    private final String position;

    public PositionResponse(final String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }
}
