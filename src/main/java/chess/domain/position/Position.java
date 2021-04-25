package chess.domain.position;

import chess.domain.piece.direction.MoveStrategies;
import chess.domain.piece.direction.MoveStrategy;
import chess.exception.InvalidDirectionException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Position implements Comparable<Position> {

    private final File file;
    private final Rank rank;

    public Position(final String input) {
        this(input.split("")[0], input.split("")[1]);
    }

    public Position(final int file, final int rank) {
        this(File.from(file).orElseThrow(() -> new IllegalArgumentException("해당하는 File 위치를 찾을 수 없습니다.")),
                Rank.from(rank).orElseThrow(() -> new IllegalArgumentException("해당하는 Rank 위치를 찾을 수 없습니다.")));
    }

    public Position(final String file, final String rank) {
        this(File.from(file).orElseThrow(() -> new IllegalArgumentException("해당하는 File 위치를 찾을 수 없습니다.")),
                Rank.from(rank).orElseThrow(() -> new IllegalArgumentException("해당하는 Rank 위치를 찾을 수 없습니다.")));
    }

    public Position(final File file, final Rank rank) {
        this.file = file;
        this.rank = rank;
    }

    public List<Integer> subtract(final Position source) {
        return Arrays.asList(file.subtract(source.file), rank.subtract(source.rank));
    }

    public boolean hasLinearPath(final Position target) {
        return isLinear(target) || isDiagonal(target);
    }

    private boolean isLinear(final Position target) {
        final List<Integer> result = target.subtract(this);
        final Difference difference = new Difference(result);
        return difference.hasZeroValue() && !difference.allZeroValue();
    }

    private boolean isDiagonal(final Position target) {
        final List<Integer> result = target.subtract(this);
        final Difference difference = new Difference(result);
        return difference.isSameAbsoluteValue() && !difference.hasZeroValue();
    }

    public MoveStrategy decideMoveStrategy(final Position target) {
        if (hasLinearPath(target)) {
            final Difference difference = directionMatcher(target);
            return MoveStrategies.matchedMoveStrategy(difference.fileDegree(), difference.rankDegree());
        }
        throw new InvalidDirectionException();
    }

    private Difference directionMatcher(final Position target) {
        final List<Integer> result = target.subtract(this);
        final Difference difference = new Difference(result);
        return difference.makeUnitLength();
    }

    public Position nextPosition(final MoveStrategy moveStrategy) {
        return new Position(file.increaseFile(moveStrategy.fileDegree()),
                rank.increaseRank(moveStrategy.rankDegree()));
    }

    public File file() {
        return file;
    }

    public Rank rank() {
        return rank;
    }

    public String position() {
        return file.symbol() + rank.symbol();
    }

    @Override
    public int compareTo(Position position) {
        if (rank == position.rank) {
            return file.value() - position.file.value();
        }
        return position.rank.value() - rank.value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Position position = (Position) o;
        return file == position.file && rank == position.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, rank);
    }

    @Override
    public String toString() {
        return "Position{" +
                "file=" + file +
                ", rank=" + rank +
                '}';
    }
}
