package chess.model.domain.board;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import util.NullChecker;

public class Square {

    public static final int MIN_FILE_AND_RANK_COUNT = 1;
    public static final int MAX_FILE_AND_RANK_COUNT;
    private static final Map<String, Square> CACHE;

    static {
        CACHE = Collections.unmodifiableMap(Arrays.stream(File.values())
            .flatMap(file -> Arrays.stream(Rank.values())
                .map(rank -> new Square(file, rank)))
            .collect(Collectors.toMap(Square::getName, Function.identity())));

        MAX_FILE_AND_RANK_COUNT = Integer.max(File.values().length, Rank.values().length);
    }

    private final File file;
    private final Rank rank;

    private Square(File file, Rank rank) {
        this.file = file;
        this.rank = rank;
    }

    public static Square of(String location) {
        NullChecker.validateNotNull(location);
        if (CACHE.containsKey(location)) {
            return CACHE.get(location);
        }
        throw new IllegalArgumentException("잘못된 입력 - Square 인자 : " + location);
    }

    public static Square of(File file, Rank rank) {
        NullChecker.validateNotNull(file, rank);
        return Square.of(file.getName() + rank.getName());
    }

    public boolean hasIncreased(int fileIncrement, int rankIncrement) {
        if (file.hasNextIncrement(fileIncrement) && rank.hasNextIncrement(rankIncrement)) {
            File nextIncrementFile = file.findIncrement(fileIncrement);
            Rank nextIncrementRank = rank.findIncrement(rankIncrement);
            return CACHE.containsKey(nextIncrementFile.getName() + nextIncrementRank.getName());
        }
        return false;
    }

    public boolean isSameFile(Square square) {
        return this.file == square.file;
    }

    public boolean isLastRank() {
        return this.rank == Rank.FIRST || this.rank == Rank.EIGHTH;
    }

    public boolean isSameRank(Rank rank) {
        return this.rank == rank;
    }

    public int calculateRankDistance(Square square) {
        return this.rank.calculateDistance(square.rank);
    }

    public int getRankCompare(Square square) {
        return Integer.compare(rank.compareTo(square.rank), 0);
    }

    public int getFileCompare(Square square) {
        return Integer.compare(file.compareTo(square.file), 0);
    }

    public Square getIncreasedSquare(int fileIncrement, int rankIncrement) {
        return Square.of(file.findIncrement(fileIncrement), rank.findIncrement(rankIncrement));
    }

    public String getName() {
        return file.getName() + rank.getName();
    }

    @Override
    public String toString() {
        return "Square{" + file.getName() + rank.getName() + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Square square = (Square) o;
        return file == square.file &&
            rank == square.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, rank);
    }
}

