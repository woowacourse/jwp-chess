package wooteco.chess.domain.piece;

import java.util.stream.Stream;

public enum Team {
    BLACK("black"),
    WHITE("white");

    private final String value;

    Team(String value) {
        this.value = value;
    }

    public Team negate() {
        if (this == BLACK) {
            return WHITE;
        }
        return BLACK;
    }

    public static Team of(String value) {
        return Stream.of(values())
                .filter(team -> value.equals(team.toString()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));
    }

    @Override
    public String toString() {
        return value;
    }
}
