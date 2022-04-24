package chess.dto;

import java.util.Objects;

public class FullGameDto {

    private final GameOverviewDto metadata;
    private final GameSnapshotDto snapshot;

    public FullGameDto(GameOverviewDto metadata, GameSnapshotDto snapshot) {
        this.metadata = metadata;
        this.snapshot = snapshot;
    }

    public GameOverviewDto getMetadata() {
        return metadata;
    }

    public GameSnapshotDto getSnapshot() {
        return snapshot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FullGameDto that = (FullGameDto) o;
        return Objects.equals(metadata, that.metadata)
                && Objects.equals(snapshot, that.snapshot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metadata, snapshot);
    }

    @Override
    public String toString() {
        return "FullGameDto{" + "metadata=" + metadata + ", snapshot=" + snapshot + '}';
    }
}
