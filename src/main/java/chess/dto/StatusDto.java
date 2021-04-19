package chess.dto;

import chess.domain.Game;

public class StatusDto {

    String whitePoint;
    String blackPoint;

    public StatusDto(Game game) {
        this.whitePoint = String.valueOf(game.computeWhitePoint());
        this.blackPoint = String.valueOf(game.computeBlackPoint());
    }

    public String getWhitePoint() {
        return whitePoint;
    }

    public void setWhitePoint(String whitePoint) {
        this.whitePoint = whitePoint;
    }

    public String getBlackPoint() {
        return blackPoint;
    }

    public void setBlackPoint(String blackPoint) {
        this.blackPoint = blackPoint;
    }
}
