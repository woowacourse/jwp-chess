package chess.dto.view;

import chess.domain.game.Game;
import chess.domain.game.statistics.GameResult;
import chess.entity.GameEntity;
import java.util.Objects;

public class GameResultDto {

    private final GameOverviewDto metadata;
    private final GameSnapshotDto snapshot;
    private final GameResult result;

    public GameResultDto(GameOverviewDto metadata, GameSnapshotDto snapshot, GameResult result) {
        this.metadata = metadata;
        this.snapshot = snapshot;
        this.result = result;
    }

    public static GameResultDto of(GameEntity gameEntity, Game game) {
        GameOverviewDto metadata = gameEntity.toDto();
        GameSnapshotDto snapshot = game.toSnapshotDto();
        GameResult result = game.getResult();
        return new GameResultDto(metadata, snapshot, result);
    }

    public GameOverviewDto getMetadata() {
        return metadata;
    }

    public GameSnapshotDto getSnapshot() {
        return snapshot;
    }

    public GameResult getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameResultDto that = (GameResultDto) o;
        return Objects.equals(metadata, that.metadata)
                && Objects.equals(snapshot, that.snapshot)
                && Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metadata, snapshot, result);
    }

    @Override
    public String toString() {
        return "GameResultDto{" +
                "metadata=" + metadata +
                ", snapshot=" + snapshot +
                ", result=" + result +
                '}';
    }
}
