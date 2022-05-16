package chess.dao.converter;

import chess.model.board.Board;
import chess.model.state.State;
import chess.model.state.finished.End;
import chess.model.state.running.BlackTurn;
import chess.model.state.running.WhiteTurn;
import java.util.Arrays;
import java.util.function.Function;

public enum StateConverter {

    WHITE_TURN("WHITE_TURN", WhiteTurn::new),
    BLACK_TURN("BLACK_TURN", BlackTurn::new),
    END("END", End::new),
    ;

    private final String name;
    private final Function<Board, State> stateGenerator;

    StateConverter(final String name, final Function<Board, State> stateGenerator) {
        this.name = name;
        this.stateGenerator = stateGenerator;
    }

    public static String convertToString(final State state) {
        return Arrays.stream(values())
                .filter(value -> value.name.equals(state.getSymbol()))
                .map(value -> value.name)
                .findAny()
                .orElseThrow();
    }

    public static State convertToState(final String stateName, final Board board) {
        return Arrays.stream(values())
                .filter(value -> value.name.equals(stateName))
                .map(value -> value.stateGenerator.apply(board))
                .findAny()
                .orElseThrow();
    }
}
