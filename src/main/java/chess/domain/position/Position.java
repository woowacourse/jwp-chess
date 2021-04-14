package chess.domain.position;

public class Position {
    private final File file;
    private final Rank rank;

    public Position(final String rank, final String file) {
        this(File.from(file).orElseThrow(() -> new IllegalArgumentException("해당하는 File 위치를 찾을 수 없습니다.")),
                Rank.from(rank).orElseThrow(() -> new IllegalArgumentException("해당하는 Rank 위치를 찾을 수 없습니다.")));
    }

    public Position(final File file, final Rank rank) {
        this.file = file;
        this.rank = rank;
    }

    public String position() {
        return file.symbol() + rank.symbol();
    }
}
