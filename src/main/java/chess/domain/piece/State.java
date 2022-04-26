package chess.domain.piece;

import chess.domain.position.Position;

import java.util.function.Function;
import java.util.stream.Stream;

public enum State {

    PAWN('P', 1, Pawn::new),
    ROOK('R', 5, Rook::new),
    KNIGHT('N', 2.5, Knight::new),
    BISHOP('B', 3, Bishop::new),
    QUEEN('Q', 9, Queen::new),
    KING('K', 0, King::new);

    private final char name;
    private final double score;
    private final Function<Position, Piece> convertPiece;

    State(char name, double score, Function<Position, Piece> convertPiece) {
        this.name = name;
        this.score = score;
        this.convertPiece = convertPiece;
    }

    public static State from(final String name) {
        return Stream.of(State.values())
                .filter(state -> state.name == name.charAt(0))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 이름의 체스말이 없습니다."));
    }

    public static Piece createPieceByState(final String name, Position position) {
        return from(name).convertPiece.apply(position);
    }

    public char getName() {
        return name;
    }

    public double getScore() {
        return score;
    }
}
