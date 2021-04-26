package chess.dao.dto.state;

public class StateDto {

    private Long id;
    private Long gameId;
    private String turnOwner;
    private int turnNumber;
    private boolean isPlaying;

    public StateDto() {
    }

    public StateDto(Long id, Long gameId, String turnOwner, int turnNumber, boolean isPlaying) {
        this.id = id;
        this.gameId = gameId;
        this.turnOwner = turnOwner;
        this.turnNumber = turnNumber;
        this.isPlaying = isPlaying;
    }

    public StateDto(Long gameId, String turnOwner, int turnNumber, boolean isPlaying) {
        this.gameId = gameId;
        this.turnOwner = turnOwner;
        this.turnNumber = turnNumber;
        this.isPlaying = isPlaying;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getTurnOwner() {
        return turnOwner;
    }

    public void setTurnOwner(String turnOwner) {
        this.turnOwner = turnOwner;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
