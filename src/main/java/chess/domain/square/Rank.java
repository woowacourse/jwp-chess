package chess.domain.square;

import java.util.Arrays;

public enum Rank {

    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8);

    public static final Rank BLACK_PAWN_INITIAL_RANK = SEVEN;
    public static final Rank WHITE_PAWN_INITIAL_RANK = TWO;

    private final int value;

    Rank(final int value) {
        this.value = value;
    }

    public static Rank from(final int value) {
        return Arrays.stream(values())
                .filter(rank -> rank.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("%d는 존재하지 않는 랭크입니다.", value)));
    }

    public Rank add(final int yDegree) {
        final int newValue = value + yDegree;
        return from(newValue);
    }

    public boolean isAddable(final int yDegree) {
        final int newValue = value + yDegree;
        return Arrays.stream(values())
                .anyMatch(rank -> rank.value == newValue);
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
