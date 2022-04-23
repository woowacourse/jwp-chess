package chess.domain.square;

import static chess.domain.square.Rank.BLACK_PAWN_INITIAL_RANK;
import static chess.domain.square.Rank.WHITE_PAWN_INITIAL_RANK;

import chess.domain.piece.detail.Direction;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Square {

    private static final Map<String, Square> squareCache = new HashMap<>();

    static {
        for (File file : File.values()) {
            initializeSquareCache(file);
        }
    }

    private final File file;
    private final Rank rank;

    public Square(final File file, final Rank rank) {
        this.file = file;
        this.rank = rank;
    }

    public static Square of(final File file, final Rank rank) {
        return squareCache.get(file.toString() + rank.toString());
    }

    public static Square from(final String rawSquare) {
        validateLength(rawSquare);
        validateExist(rawSquare);
        return squareCache.get(rawSquare);
    }

    private static void initializeSquareCache(final File file) {
        for (Rank rank : Rank.values()) {
            squareCache.put(file.toString() + rank.toString(), new Square(file, rank));
        }
    }

    private static void validateExist(final String rawSquare) {
        if (!squareCache.containsKey(rawSquare)) {
            throw new IllegalArgumentException("존재하지 않는 스퀘어입니다.");
        }
    }

    private static void validateLength(final String rawSquare) {
        if (rawSquare.length() != 2) {
            throw new IllegalArgumentException("스퀘어의 길이가 올바르지 않습니다.");
        }
    }

    public Square next(final Direction direction) {
        final File file = this.file.add(direction.getXDegree());
        final Rank rank = this.rank.add(direction.getYDegree());

        return Square.of(file, rank);
    }

    public Square next(final Direction direction, final int count) {
        final File file = this.file.add(direction.getXDegree() * count);
        final Rank rank = this.rank.add(direction.getYDegree() * count);

        return Square.of(file, rank);
    }

    public int calculateDistance(final Square to) {
        final int fileDifference = Math.abs(this.file.getValue() - to.file.getValue());
        final int rankDifference = Math.abs(this.rank.getValue() - to.rank.getValue());
        return Math.max(fileDifference, rankDifference);
    }

    public boolean isExist(final Direction direction) {
        return file.isAddable(direction.getXDegree()) && rank.isAddable(direction.getYDegree());
    }

    public boolean isExist(final Direction direction, final int count) {
        return file.isAddable(direction.getXDegree() * count) && rank.isAddable(direction.getYDegree() * count);
    }

    public boolean isBlackPawnInitial() {
        return rank == BLACK_PAWN_INITIAL_RANK;
    }

    public boolean isWhitePawnInitial() {
        return rank == WHITE_PAWN_INITIAL_RANK;
    }

    public String toRawSquare() {
        return file.toString() + rank.toString();
    }

    public File getFile() {
        return file;
    }

    public Rank getRank() {
        return rank;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Square)) {
            return false;
        }
        final Square square = (Square) o;
        return file == square.file && rank == square.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, rank);
    }

    @Override
    public String toString() {
        return "Square{" +
                "file=" + file +
                ", rank=" + rank +
                '}';
    }
}
