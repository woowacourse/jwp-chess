package chess.domain.position;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum Rank {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8);

    private final int rank;

    Rank(int rank) {
        this.rank = rank;
    }

    public static Rank toRank(char candidate) {
        return Arrays.stream(Rank.values())
                .filter(rank -> rank.getRank() == Character.getNumericValue(candidate))
                .findAny()
                .orElseThrow(IllegalStateException::new);
    }

    public static Rank toRank(int candidate) {
        return Arrays.stream(Rank.values())
                .filter(rank -> rank.getRank() == candidate)
                .findAny()
                .orElseThrow(IllegalStateException::new);
    }

    public static boolean isRank(int candidate) {
        return Arrays.stream(Rank.values())
                .anyMatch(rank -> rank.getRank() == candidate);
    }

    public int calculateRank(Rank other) {
        return rank - other.getRank();
    }

    public int calculateAbsoluteValue(Rank other) {
        return Math.abs(rank - other.getRank());
    }

    public int getRank() {
        return rank;
    }

    public static List<Rank> reverseValues() {
        return Arrays.stream(Rank.values())
            .sorted(Comparator.reverseOrder())
            .collect(Collectors.toList());
    }
}
