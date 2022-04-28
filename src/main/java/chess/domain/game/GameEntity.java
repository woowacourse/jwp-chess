package chess.domain.game;

public class GameEntity {
    private String gameID;
    private String gameCode;
    private boolean isFinished;

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public void setIsFinished(String turn) {
        this.isFinished = (turn.equals("FINISHED") || turn.equals("READY"));
    }

    public String getGameID() {
        return gameID;
    }

    public String getGameCode() {
        return gameCode;
    }

    public boolean getIsFinished() {
        return isFinished;
    }
}
