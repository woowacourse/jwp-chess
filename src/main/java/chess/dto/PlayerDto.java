package chess.dto;

import chess.domain.player.Player;

public class PlayerDto {
    private final Player whitePlayer;
    private final Player blackPlayer;

    public PlayerDto(final Player whitePlayer, final Player blackPlayer) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }
}
