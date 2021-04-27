package chess.domain.room;

public class Players {
    private String whitePlayer;
    private String blackPlayer;

    public Players() {
    }

    public Players(final String whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    public Players(final String whitePlayer, final String blackPlayer) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
    }

    public void join(String blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    public boolean startable() {
        if (whitePlayer == null) {
            return false;
        }

        return blackPlayer != null;
    }

    public String getWhitePlayer() {
        return whitePlayer;
    }

    public void setBlackPlayer(final String blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    public String getBlackPlayer() {
        return blackPlayer;
    }
}
