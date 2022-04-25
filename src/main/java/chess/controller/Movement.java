package chess.controller;

import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import java.util.Map;
import java.util.regex.Pattern;

public class Movement {

    private final Pattern pattern = Pattern.compile("[A-H][1-8]");

    private final Position from;
    private final Position to;

    private static final Map<String, Rank> RANKS = Map.of(
            "1", Rank.ONE, "2", Rank.TWO, "3", Rank.THREE, "4", Rank.FOUR, "5", Rank.FIVE, "6", Rank.SIX, "7", Rank.SEVEN, "8", Rank.EIGHT
    );
    private static final Map<String, File> FILES = Map.of(
            "A", File.A, "B", File.B, "C", File.C, "D", File.D, "E", File.E, "F", File.F, "G", File.G, "H", File.H
    );

    public Movement(String from, String to) {
        checkPosition(from);
        checkPosition(to);
        this.from = new Position(file(from), rank(from));
        this.to = new Position(file(to), rank(to));
    }

    private void checkPosition(String position) {
        if (!pattern.matcher(position).matches()) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
    }

    private File file(String position) {
        return FILES.get(position.substring(0, 1));
    }

    private Rank rank(String position) {
        return RANKS.get(position.substring(1, 2));
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }
}
