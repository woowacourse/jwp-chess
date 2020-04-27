package chess.result;

import chess.player.Player;

public class ChessResult {
    private static final String DRAW_RESULT_NAME = "승자 없음";

    private final Result result;
    private final String name;

    public ChessResult(Result result, String name) {
        this.result = result;
        this.name = name;
    }

    public ChessResult(Result result) {
        this(result, DRAW_RESULT_NAME);
    }

    public static ChessResult of(Player white, Player black) {
        return ChessResultCreater.findWinner(white, black);
    }

    public boolean isDraw() {
        return result.isDraw();
    }

    public String getName() {
        return name;
    }
}
