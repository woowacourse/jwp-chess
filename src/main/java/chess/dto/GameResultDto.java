package chess.dto;

import java.util.Objects;

public class GameResultDto {

    private final Integer winCount;
    private final Integer drawCount;
    private final Integer loseCount;

    public GameResultDto(Integer winCount, Integer drawCount, Integer loseCount) {
        this.winCount = winCount;
        this.drawCount = drawCount;
        this.loseCount = loseCount;
    }

    public Integer getWinCount() {
        return winCount;
    }

    public Integer getDrawCount() {
        return drawCount;
    }

    public Integer getLoseCount() {
        return loseCount;
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
        return winCount == that.winCount &&
            drawCount == that.drawCount &&
            loseCount == that.loseCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(winCount, drawCount, loseCount);
    }
}
