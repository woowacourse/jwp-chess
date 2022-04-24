package chess.dto;

import chess.domain.game.statistics.GameResult;
import java.util.Objects;

public class GameResultDto {

    private final FullGameDto game;
    private final GameResult result;

    public GameResultDto(FullGameDto game, GameResult result) {
        this.game = game;
        this.result = result;
    }

    public String getName() {
        return game.getName();
    }

    public GameSnapshotDto getGameInfo() {
        return game.getSnapshot();
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
        return Objects.equals(game, that.game)
                && Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(game, result);
    }

    @Override
    public String toString() {
        return "GameResultDto{" +
                "game=" + game +
                ", result=" + result +
                '}';
    }
}
