package wooteco.chess.domain.piece;

import java.util.stream.Stream;

public enum Team {

    BLACK("black"),
    WHITE("white");

    private final String value;

    Team(final String value) {
        this.value = value;
    }

    public static Team of(final String value) {
        return Stream.of(values())
                .filter(team -> value.equals(team.toString()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));
    }

    public Team reverse() {
        if (this == BLACK) {
            return WHITE;
        }
        return BLACK;
    }

    @Override
    public String toString() {
        return value;
    }
}
