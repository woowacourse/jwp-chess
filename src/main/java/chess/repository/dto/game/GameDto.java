package chess.repository.dto.game;

public class GameDto {

    private final Long id;
    private final String title;
    private final String password;
    private final Long player_id1;
    private final Long player_id2;
    private final Boolean finished;
    private final String currentTurnColor;

    public GameDto(final Long id, final String title, final String password,
                   final Long player_id1, final Long player_id2,
                   final Boolean finished, final String currentTurnColor) {
        this.id = id;
        this.title = title;
        this.password = password;
        this.player_id1 = player_id1;
        this.player_id2 = player_id2;
        this.finished = finished;
        this.currentTurnColor = currentTurnColor;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }

    public Long getPlayer_id1() {
        return player_id1;
    }

    public Long getPlayer_id2() {
        return player_id2;
    }

    public Boolean getFinished() {
        return finished;
    }

    public String getCurrentTurnColor() {
        return currentTurnColor;
    }
}
