package chess.domain.position;

public class Position implements Comparable<Position> {
    private final File file;
    private final Rank rank;

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
}
