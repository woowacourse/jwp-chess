package wooteco.chess.domain.coordinate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum Rank {
    EIGHT(8),
    SEVEN(7),
    SIX(6),
    FIVE(5),
    FOUR(4),
    THREE(3),
    TWO(2),
    ONE(1);

    private static final Map<Integer, Rank> byValue = new HashMap<>();

    static {
        for (Rank rank : values()) {
            byValue.put(rank.value, rank);
        }
    }

    private final int value;

    Rank(final int value) {
        this.value = value;
    }

    public static Rank findByValue(int value) {
        if (!byValue.containsKey(value)) {
            throw new IllegalArgumentException("rank : " + value + ", file의 value는 1부터 8까지 입니다.");
        }
        return byValue.get(value);
    }

    public int subtract(Rank rank) {
        return value - rank.value;
    }

    public Rank sum(int rankValue) {
        return findByValue(this.value + rankValue);
    }

    public int getValue() {
        return value;
    }
}
