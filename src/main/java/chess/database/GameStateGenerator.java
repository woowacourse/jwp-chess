package chess.database;

import java.util.Map;
import java.util.function.BiFunction;

import chess.domain.Color;
import chess.domain.board.Board;
import chess.domain.game.Finished;
import chess.domain.game.GameState;
import chess.domain.game.Ready;
import chess.domain.game.Running;

public class GameStateGenerator {
    private static final Map<String, BiFunction<Board, Color, GameState>> STATE_GENERATOR
        = Map.of(
        "READY", Ready::new,
        "RUNNING", Running::new,
        "FINISHED", Finished::new
    );

    public static GameState generate(Board board, String state, String turnColor) {
        return STATE_GENERATOR.get(state).apply(board, Color.of(turnColor));
    }
}
