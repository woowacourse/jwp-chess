package chess.domain.player;

import chess.domain.piece.PieceColor;
import chess.exception.InvalidPlayerException;
import java.util.List;

public class Players {

    private final List<Player> players;

    public Players(List<Player> players) {
        this.players = players;
    }

    public static Players of(List<Player> players) {
        return new Players(players);
    }

    public Player currentPlayer(PieceColor currentColor) {
        return players.stream()
            .filter(player -> player.isColor(currentColor))
            .findAny()
            .orElseThrow(InvalidPlayerException::new);
    }
}
