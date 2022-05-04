package chess.domain.game.state;

import chess.domain.Color;
import chess.domain.player.Players;

public class GameStateFactory {

    public static GameState loadGameState(final Players players, final boolean finished, final Color currentTurnColor) {
        if (finished) {
            return new FinishedState(players, currentTurnColor);
        }
        if (players.isPlayerAbleToPromotePawn(currentTurnColor.reverse())) {
            return new RunningState(players, currentTurnColor.reverse());
        }
        return new RunningState(players, currentTurnColor);
    }
}
