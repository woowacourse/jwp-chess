package wooteco.chess.domain.coordinate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum Direction {
    LEFT_UP(-1, 1),
    UP(0, 1),
    RIGHT_UP(1, 1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    LEFT_DOWN(-1, -1),
    DOWN(0, -1),
    RIGHT_DOWN(1, -1);

    private final int fileVariation;
    private final int rankVariation;

    private static final Map<Integer, Direction> byVariations = new HashMap<>();

    static {
        for (Direction direction : values()) {
            int key = makeKey(direction.fileVariation, direction.rankVariation);
            byVariations.put(key, direction);
        }
    }

    Direction(final int fileVariation, final int rankVariation) {
        this.fileVariation = fileVariation;
        this.rankVariation = rankVariation;
    }

    public static Direction findByVariations(int fileVariation, int rankVariation) {
        if (!byVariations.containsKey(makeKey(fileVariation, rankVariation))) {
            throw new IllegalArgumentException("fileVariation : " + fileVariation
                    + ", rankVariation : " + rankVariation
                    + ", 입력값을 확인하시오.");
        }
        return byVariations.get(makeKey(fileVariation, rankVariation));
    }

    private static int makeKey(int fileVariation, int rankVariation) {
        return fileVariation * 10 + rankVariation;
    }

    public int getFileVariation() {
        return fileVariation;
    }

    public int getRankVariation() {
        return rankVariation;
    }
}
