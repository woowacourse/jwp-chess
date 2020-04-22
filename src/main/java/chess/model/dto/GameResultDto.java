package chess.model.dto;

import java.util.Objects;

public class GameResultDto {

    private final int winCount;
    private final int drawCount;
    private final int loseCount;

    public GameResultDto(int winCount, int drawCount, int loseCount) {
        this.winCount = winCount;
        this.drawCount = drawCount;
        this.loseCount = loseCount;
    }

    public int getWinCount() {
        return winCount;
    }

    public int getDrawCount() {
        return drawCount;
    }

    public int getLoseCount() {
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
