package chess.result;

import chess.exception.InvalidConstructorValueException;
import chess.player.Player;

import java.util.Objects;

public class ChessResult {
    private static final String DRAW_RESULT_NAME = "승자 없음";

    private final Result result;
    private final String name;

    public ChessResult(Result result, String name) {
        if (Objects.isNull(result) || Objects.isNull(name) || name.isEmpty()) {
            throw new InvalidConstructorValueException();
        }
        this.result = result;
        this.name = name;
    }

    public static ChessResult of(Player white, Player black) {
        return ChessResultCreater.findWinner(white, black);
    }

    public ChessResult(Result result) {
        this(result, DRAW_RESULT_NAME);
    }

    public boolean isDraw() {
        return result.isDraw();
    }

    public String getName() {
        return name;
    }
}
