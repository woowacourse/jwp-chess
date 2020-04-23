package wooteco.chess.domain.position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Positions {
    private static final int RANK_INDEX = 1;
    private static final int FILE_INDEX = 0;
    private static List<Position> POSITIONS = new ArrayList<>();

    static {
        for (Rank rank : Rank.values()) {
            addPosition(rank);
        }
    }

    private static void addPosition(Rank rank) {
        for (File file : File.values()) {
            POSITIONS.add(new Position(file, rank));
        }
    }

    public static Position of(final String position) {
        File file = File.of(position.substring(FILE_INDEX, RANK_INDEX));
        Rank rank = Rank.of(position.substring(RANK_INDEX));

        return findPosition(file, rank);
    }

    public static Position of(final int fileSymbol, final int rankSymbol) {
        return findPosition(File.of(fileSymbol), Rank.of(rankSymbol));
    }

    public static Position of(final File file, final Rank rank) {
        return findPosition(file, rank);
    }

    private static Position findPosition(File file, Rank rank) {
        return POSITIONS.stream()
                .filter(position -> position.equals(new Position(file, rank)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 좌표값을 입력하셨습니다."));
    }

    public static List<String> get() {
        List<String> parseResult = POSITIONS.stream()
                .map(Position::toString)
                .collect(Collectors.toList());
        return Collections.unmodifiableList(parseResult);
    }

    public static List<Position> getValue() {
        return Collections.unmodifiableList(POSITIONS);
    }
}
