package chess.dto;

public class PlayersDto {
    private final String whitePlayer;
    private final String blackPlayer;

    public PlayersDto(final String whitePlayer, final String blackPlayer) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
    }

    public String getWhitePlayer() {
        return whitePlayer;
    }

    public String getBlackPlayer() {
        return blackPlayer;
    }
}
