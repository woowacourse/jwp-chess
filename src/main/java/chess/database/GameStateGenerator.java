package chess.database;

import chess.database.dto.GameStateDto;
import chess.domain.Color;
import chess.domain.board.Board;
import chess.domain.game.Finished;
import chess.domain.game.GameState;
import chess.domain.game.Ready;
import chess.domain.game.Running;
import chess.domain.game.State;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class GameStateGenerator {

//    private static final Map<String, BiFunction<Board, Color, GameState>> STATE_GENERATOR
//        = Map.of(
//        "READY", Ready::new,
//        "RUNNING", Running::new,
//        "FINISHED", Finished::new
//    );
    private static final Map<State, BiFunction<Board, Color, GameState>> STATE_GENERATOR
        = new EnumMap<>(Map.of(
        State.READY, Ready::new,
        State.RUNNING, Running::new,
        State.FINISHED, Finished::new
    ));
    private static final int STATE_AND_COLOR_SIZE = 2;
    private static final int STATE_INDEX = 0;
    private static final int COLOR_INDEX = 1;

//    public static GameState generate(Board board, List<String> stateAndColor) {
//        validateSize(stateAndColor);
//        return STATE_GENERATOR.get(stateAndColor.get(STATE_INDEX))
//            .apply(board, Color.of(stateAndColor.get(COLOR_INDEX)));
//    }

    public static GameState generate(Board board, GameStateDto stateAndColor) {
//        validateSize(stateAndColor);
        return STATE_GENERATOR.get(stateAndColor.getState())
            .apply(board, Color.of(stateAndColor.getTurnColor().name()));
    }

//    private static void validateSize(List<String> stateAndColor) {
//        if (stateAndColor.size() != STATE_AND_COLOR_SIZE) {
//            throw new IllegalArgumentException(
//                String.format("[ERROR] state와 color는 %d 개 여야 합니다.", STATE_AND_COLOR_SIZE)
//            );
//        }
//    }
}
