package chess.web.controller.dto.request;


public class MoveRequestDTO {

    private Long gameId;
    private String startPositionInput;
    private String destinationInput;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getStartPositionInput() {
        return startPositionInput;
    }

    public void setStartPositionInput(String startPositionInput) {
        this.startPositionInput = startPositionInput;
    }

    public String getDestinationInput() {
        return destinationInput;
    }

    public void setDestinationInput(String destinationInput) {
        this.destinationInput = destinationInput;
    }
}
