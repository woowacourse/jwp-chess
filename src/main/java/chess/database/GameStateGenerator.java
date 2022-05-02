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
import java.util.Map;
import java.util.function.BiFunction;

public class GameStateGenerator {

    private static final Map<State, BiFunction<Board, Color, GameState>> STATE_GENERATOR
        = new EnumMap<>(Map.of(
        State.READY, Ready::new,
        State.RUNNING, Running::new,
        State.FINISHED, Finished::new)
    );

    public static GameState generate(Board board, GameStateDto stateAndColor) {
        return STATE_GENERATOR.get(stateAndColor.getState())
            .apply(board, Color.of(stateAndColor.getTurnColor().name()));
    }
}
