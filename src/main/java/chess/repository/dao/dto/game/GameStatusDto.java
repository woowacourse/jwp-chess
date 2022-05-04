package chess.repository.dao.dto.game;

public class GameStatusDto {

    private final Long id;
    private final String title;
    private final Boolean finished;

    public GameStatusDto(final Long id, final String title, final Boolean finished) {
        this.id = id;
        this.title = title;
        this.finished = finished;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getFinished() {
        return finished;
    }
}
