package chess.domain.position;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public enum File {
    A("a", 1),
    B("b", 2),
    C("c", 3),
    D("d", 4),
    E("e", 5),
    F("f", 6),
    G("g", 7),
    H("h", 8);

    private final String symbol;
    private final int value;

    File(final String symbol, final int value) {
        this.symbol = symbol;
        this.value = value;
    }

    public static Optional<File> from(final String symbol) {
        return Arrays.stream(values())
                .filter(file -> file.symbol.equals(symbol))
                .findFirst();
    }

    public static Optional<File> from(final int value) {
        return Arrays.stream(values())
                .filter(file -> file.value == value)
                .findFirst();
    }

    public int subtract(final File file) {
        return value - file.value;
    }

    public static List<String> fileSymbols() {
        return Arrays.stream(values())
                .map(File::symbol)
                .collect(Collectors.toList());
    }

    public int increaseFile(final int degree) {
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
        return "File{" +
                "symbol='" + symbol + '\'' +
                ", value=" + value +
                '}';
    }
}

