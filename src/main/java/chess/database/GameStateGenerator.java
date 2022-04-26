package chess.database;

import java.util.Map;
import java.util.function.BiFunction;

import chess.database.dto.GameStateDto;
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
    private static final int STATE_AND_COLOR_SIZE = 2;
    private static final int STATE_INDEX = 0;
    private static final int COLOR_INDEX = 1;

    public static GameState generate(Board board, GameStateDto gameStateDto) {
        return STATE_GENERATOR.get(gameStateDto.getState())
            .apply(board, Color.of(gameStateDto.getTurnColor()));
    }
}
