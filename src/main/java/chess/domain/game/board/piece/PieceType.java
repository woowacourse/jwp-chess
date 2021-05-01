package chess.domain.game.board.piece;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

public enum PieceType {
    KING('k', 0.0),
    QUEEN('q', 9.0),
    BISHOP('b', 3.0),
    KNIGHT('n', 2.5),
    PAWN('p', 1.0),
    ROOK('r', 5.0);

    private final char value;
    private final double score;

    PieceType(final char value, final double score) {
        this.value = value;
        this.score = score;
    }

    @JsonCreator
    public static PieceType from(final char value) {
        return Arrays.stream(values())
            .filter(pieceType -> pieceType.value == value)
            .findAny()
            .orElseThrow(() -> new PieceTypeNotFoundException(value));
    }

    @JsonValue
    public char getValue() {
        return value;
    }

    public double getScore() {
        return score;
    }

}
