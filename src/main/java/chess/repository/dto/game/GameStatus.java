package chess.repository.dto.game;

public class GameStatus {

    private final Long id;
    private final String title;
    private final Boolean finished;

    public GameStatus(final Long id, final String title, final Boolean finished) {
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
