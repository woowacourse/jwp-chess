package chess.controller.dto.response;

public class GameStatusResponse {

    private final Long id;
    private final String title;
    private final Boolean finished;

    public GameStatusResponse(final Long id, final String title, final Boolean finished) {
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
