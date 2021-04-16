package chess.spring.controller.dto.request;


public class MoveRequestDTO {
    private Long gameId;
    private String startPosition;
    private String destination;

    public MoveRequestDTO() {
    }

    public MoveRequestDTO(Long gameId, String startPosition, String destination) {
        this.gameId = gameId;
        this.startPosition = startPosition;
        this.destination = destination;
    }

    public MoveRequestDTO(String startPosition, String destination) {
        this.startPosition = startPosition;
        this.destination = destination;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getGameId() {
        return gameId;
    }

    public String getStartPosition() {
        return startPosition;
    }

    public String getDestination() {
        return destination;
    }
}
