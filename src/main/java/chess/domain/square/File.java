package chess.domain.square;

import java.util.Arrays;

public enum File {
    A('a'),
    B('b'),
    C('c'),
    D('d'),
    E('e'),
    F('f'),
    G('g'),
    H('h'),
    ;

    private final char value;

    File(final char value) {
        this.value = value;
    }

    public static File from(final char value) {
        return Arrays.stream(values())
                .filter(file -> file.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("%c는 존재하지 않는 파일입니다.", value)));
    }

    public File add(final int xDegree) {
        final int newValue = value + xDegree;
        return from((char) newValue);
    }

    public boolean isAddable(final int xDegree) {
        final int newValue = value + xDegree;
        return Arrays.stream(values())
                .anyMatch(file -> file.value == newValue);
    }

    public char getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
