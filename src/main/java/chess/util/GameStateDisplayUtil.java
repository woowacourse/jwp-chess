package chess.util;

import chess.domain.game.GameState;
import java.util.HashMap;
import java.util.Map;

public class GameStateDisplayUtil {

    private static final Map<GameState, String> displayMap = initDisplayMap();

    private GameStateDisplayUtil() {
    }

    private static Map<GameState, String> initDisplayMap() {
        return new HashMap<>() {{
            put(GameState.WHITE_TURN, "White Turn");
            put(GameState.BLACK_TURN, "Black Turn");
            put(GameState.OVER, "종료");
        }};
    }

    public static String toDisplay(GameState gameState) {
        return displayMap.get(gameState);
    }
}
