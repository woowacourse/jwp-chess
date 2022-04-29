package chess.repository.dao.dto.game;

public class GameStatusDto {

    private final Long id;
    private final Boolean finished;

    public GameStatusDto(final Long id, final Boolean finished) {
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
