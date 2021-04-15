package chess.domain.position;

import java.util.Objects;

public class Position implements Comparable<Position> {
    private final File file;
    private final Rank rank;

    public Position(final String input) {
        this(input.split("")[0], input.split("")[1]);
    }

    public Position(final String file, final String rank) {
        this(File.from(file).orElseThrow(() -> new IllegalArgumentException("해당하는 File 위치를 찾을 수 없습니다.")),
                Rank.from(rank).orElseThrow(() -> new IllegalArgumentException("해당하는 Rank 위치를 찾을 수 없습니다.")));
    }

    public Position(final File file, final Rank rank) {
        this.file = file;
        this.rank = rank;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
