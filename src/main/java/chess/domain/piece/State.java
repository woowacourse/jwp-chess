package chess.domain.piece;

import chess.domain.position.Position;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public enum State {

    PAWN('P', 1, (state, position) -> new Pawn(position)),
    ROOK('R', 5, (state, position) -> new Rook(position)),
    KNIGHT('N', 2.5, (state, position) -> new Knight(position)),
    BISHOP('B', 3, (state, position) -> new Bishop(position)),
    QUEEN('Q', 9, (state, position) -> new Queen(position)),
    KING('K', 0, (state, position) -> new King(position));

    private final char name;
    private final double score;
    private final BiFunction<State, Position, Piece> condition;

    State(char name, double score, BiFunction<State, Position, Piece> condition) {
        this.name = name;
        this.score = score;
        this.condition = condition;
    }

    public static State from(final String name) {
        return Stream.of(State.values())
                .filter(state -> state.name == name.charAt(0))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 이름의 체스말이 없습니다."));
    }

    public static Piece createPieceByState(State inputState, Position position) {
        return Arrays.stream(values())
                .filter(state -> state == inputState)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 상태의 체스말이 없습니다."))
                .condition
                .apply(inputState, position);
    }

    public char getName() {
        return name;
    }

    public double getScore() {
        return score;
    }
}
