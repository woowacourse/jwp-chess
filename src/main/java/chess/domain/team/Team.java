package chess.domain.team;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

public enum Team {
    WHITE("white"),
    BLACK("black");

    private final String value;

    Team(final String value) {
        this.value = value;
    }

    @JsonCreator
    public static Team from(final String value) {
        return Arrays.stream(values())
            .filter(team -> team.value.equals(value))
            .findAny()
            .orElseThrow(() -> new TeamNotFoundException(value));
    }

    public Team reverse() {
        if (this.equals(WHITE)) {
            return BLACK;
        }
        return WHITE;
    }

    public boolean isBlack() {
        return this == BLACK;
    }

    public boolean isWhite() {
        return this == WHITE;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

}
