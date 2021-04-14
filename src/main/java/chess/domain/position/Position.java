package chess.domain.position;

public class Position {
    private final Rank rank;
    private final File file;

    public Position(final String rank, final String file) {
        this(Rank.from(rank).orElseThrow(() -> new IllegalArgumentException("해당하는 Rank 위치를 찾을 수 없습니다.")),
                File.from(file).orElseThrow(() -> new IllegalArgumentException("해당하는 File 위치를 찾을 수 없습니다.")));
    }

    public Position(final Rank rank, final File file) {
        this.rank = rank;
        this.file = file;
    }

    public String position() {
        return rank.symbol() + file.symbol();
    }
}
