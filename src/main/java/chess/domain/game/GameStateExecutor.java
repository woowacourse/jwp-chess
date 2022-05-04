package chess.domain.game;

import java.util.Map;
import java.util.function.Function;

public class GameStateExecutor {

    private static final Map<String, Function<GameState, GameState>> EXECUTOR =
        Map.of(
            "start", GameState::start,
            "end", GameState::finish
        );

    public static GameState execute(GameState state, String command) {
        validateCommand(command);
        return EXECUTOR.get(command).apply(state);
    }

    private static void validateCommand(String command) {
        if (!EXECUTOR.containsKey(command)) {
            throw new IllegalArgumentException("[ERROR] 실행 가능한 명령이 없습니다.");
        }
    }
}
