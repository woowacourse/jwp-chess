package chess.dto.view;

import chess.domain.game.Game;
import chess.domain.game.statistics.GameResult;
import chess.entity.GameEntity;
import java.util.Objects;

public class GameResultDto {

    private final FullGameDto fullGame;
    private final GameResult result;

    public GameResultDto(FullGameDto fullGame, GameResult result) {
        this.fullGame = fullGame;
        this.result = result;
    }

    public static GameResultDto of(GameEntity gameEntity, Game game) {
        FullGameDto fullGameDto = new FullGameDto(gameEntity.toDto(), game.toSnapshotDto());
        return new GameResultDto(fullGameDto, game.getResult());
    }

    public GameOverviewDto getMetadata() {
        return fullGame.getMetadata();
    }

    public GameSnapshotDto getSnapshot() {
        return fullGame.getSnapshot();
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
        return Objects.equals(fullGame, that.fullGame)
                && Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullGame, result);
    }

    @Override
    public String toString() {
        return "GameResultDto{" + "fullGame=" + fullGame + ", result=" + result + '}';
    }
}
