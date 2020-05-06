package chess.model.domain.piece;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Type {
    ROOK("rook", 5, true,
        Arrays.asList(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT)),
    KNIGHT("knight", 2.5, true,
        Arrays.asList(
            Direction.UP_LEFT_L, Direction.UP_RIGHT_L, Direction.DOWN_LEFT_L, Direction.DOWN_RIGHT_L, Direction.RIGHT_DOWN_L, Direction.RIGHT_UP_L,
            Direction.LEFT_DOWN_L, Direction.LEFT_UP_L)),
    BISHOP("bishop", 3, true,
        Arrays.asList(
            Direction.LEFT_UP, Direction.LEFT_DOWN, Direction.RIGHT_UP, Direction.RIGHT_DOWN)),
    QUEEN("queen", 9, true,
        Arrays.asList(
            Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT, Direction.LEFT_UP, Direction.LEFT_DOWN, Direction.RIGHT_UP, Direction.RIGHT_DOWN)),
    KING("king", 0, false,
        Arrays.asList(
            Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT, Direction.LEFT_UP, Direction.LEFT_DOWN, Direction.RIGHT_UP, Direction.RIGHT_DOWN)),
    PAWN("pawn", 1, false, Collections.singletonList(Direction.UP));

    private final String letter;
    private final double score;
    private final boolean changeFromPawn;
    private final List<Direction> directions;

    Type(String letter, double score, boolean changeFromPawn, List<Direction> directions) {
        this.letter = letter;
        this.score = score;
        this.changeFromPawn = changeFromPawn;
        this.directions = directions;
    }

    public static Type of(String letter) {
        return Arrays.stream(Type.values())
            .filter(type -> type.letter.equals(letter))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    public List<Direction> getDirections() {
        return directions;
    }

    public double getScore() {
        return score;
    }

    public boolean canPromote() {
        return changeFromPawn;
    }
}
