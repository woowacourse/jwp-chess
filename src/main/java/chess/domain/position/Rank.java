package chess.domain.position;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public enum Rank {
    ONE("1", 1),
    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6", 6),
    SEVEN("7", 7),
    EIGHT("8", 8);

    private final String symbol;
    private final int value;

    Rank(final String symbol, final int value) {
        this.symbol = symbol;
        this.value = value;
    }

    public static Optional<Rank> from(final String symbol) {
        return Arrays.stream(values())
                .filter(rank -> rank.symbol.equals(symbol))
                .findFirst();
    }

    public static Optional<Rank> from(final int value) {
        return Arrays.stream(values())
                .filter(rank -> value == rank.value)
                .findFirst();
    }

    public int subtract(final Rank rank) {
        return value - rank.value;
    }

    public static List<String> rankSymbols() {
        return Arrays.stream(values())
                .map(Rank::symbol)
                .collect(Collectors.toList());
    }

    public int increaseRank(final int degree) {
        return value + degree;
    }

    public String symbol() {
        return symbol;
    }

    public int value() {
        return value;
    }

    @Override
    public String toString() {
        return "Rank{" +
                "symbol='" + symbol + '\'' +
                ", value=" + value +
                '}';
    }
}
