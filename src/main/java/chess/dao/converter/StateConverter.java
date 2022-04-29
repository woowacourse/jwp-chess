package chess.dao.converter;

import chess.model.board.Board;
import chess.model.state.State;
import chess.model.state.finished.End;
import chess.model.state.running.BlackTurn;
import chess.model.state.running.WhiteTurn;
import java.util.Arrays;

public enum StateConverter {

    WHITE_TURN("WHITE_TURN"),
    BLACK_TURN("BLACK_TURN"),
    END("END"),
    ;

    private final String name;

    StateConverter(final String name) {
        this.name = name;
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
                .map(value -> value.createState(stateName, board))
                .findAny()
                .orElseThrow();
    }

    public State createState(final String stateName, final Board board) {
        if (stateName.equals("WHITE_TURN")) {
            return new WhiteTurn(board);
        }
        if (stateName.equals("BLACK_TURN")) {
            return new BlackTurn(board);
        }
        return new End(board);
    }
}
