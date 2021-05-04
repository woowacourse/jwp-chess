package chess.domain.room;

public class Players {
    private final String whitePlayer;
    private final String blackPlayer;

    public Players(final String whitePlayer) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = null;
    }

    public Players(final String whitePlayer, final String blackPlayer) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
    }

    public boolean isEmpty() {
        return whitePlayer == null && blackPlayer == null;
    }

    public boolean enterable() {
        return whitePlayer != null && blackPlayer == null;
    }

    public String getWhitePlayer() {
        return whitePlayer;
    }

    public String getBlackPlayer() {
        return blackPlayer;
    }
}
