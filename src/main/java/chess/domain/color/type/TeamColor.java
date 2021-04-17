package chess.domain.color.type;

import java.util.Arrays;

public enum TeamColor {
    WHITE("white", "백"),
    BLACK("black", "흑");

    private final String value;
    private final String name;

    TeamColor(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public static TeamColor of(String colorValue) {
        return Arrays.stream(values())
            .filter(teamColor -> teamColor.value.equals(colorValue))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀 색깔 입니다."));
    }

    public static TeamColor findByPieceValue(String pieceValue) {
        if (pieceValue.toUpperCase().equals(pieceValue)) {
            return BLACK;
        }
        return WHITE;
    }

    public TeamColor oppositeTeamColor() {
        if (this == WHITE) {
            return BLACK;
        }
        return WHITE;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getOppositeTeamColorName() {
        return oppositeTeamColor().getName();
    }
}
