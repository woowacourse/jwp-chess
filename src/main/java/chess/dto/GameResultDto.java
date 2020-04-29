package chess.dto;

import chess.model.repository.ResultEntity;

public class GameResultDto {

    private final Integer winCount;
    private final Integer drawCount;
    private final Integer loseCount;

    public GameResultDto(ResultEntity resultEntity) {
        this.winCount = resultEntity.getWin();
        this.drawCount = resultEntity.getDraw();
        this.loseCount = resultEntity.getLose();
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
}
