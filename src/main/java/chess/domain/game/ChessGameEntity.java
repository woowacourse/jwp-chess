package chess.domain.game;

public class ChessGameEntity {
    private Long id;
    private String state;
    private String title;

    public ChessGameEntity(final Long id, final String state, String title) {
        this.id = id;
        this.state = state;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getTitle() {
        return title;
    }

    public boolean isPlaying() {
        return state.equals("BlackTurn") || state.equals("WhiteTurn");
    }

}
