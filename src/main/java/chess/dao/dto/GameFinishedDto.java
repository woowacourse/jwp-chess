package chess.dao.dto;

public class GameFinishedDto {

    private final Long id;
    private final Boolean finished;

    public GameFinishedDto(final Long id, final Boolean finished) {
        this.id = id;
        this.finished = finished;
    }

    public Long getId() {
        return id;
    }

    public Boolean getFinished() {
        return finished;
    }
}
