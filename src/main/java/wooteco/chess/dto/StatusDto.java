package wooteco.chess.dto;

import wooteco.chess.domain.game.Score;
import wooteco.chess.domain.piece.Color;

public class StatusDto {
    private Score whiteScore;
    private Score blackScore;
    private Color winner;

    public StatusDto(Score whiteScore, Score blackScore, Color winner) {
        this.whiteScore = whiteScore;
        this.blackScore = blackScore;
        this.winner = winner;
    }

    public Score getWhiteScore() {
        return whiteScore;
    }

    public void setWhiteScore(Score whiteScore) {
        this.whiteScore = whiteScore;
    }

    public Score getBlackScore() {
        return blackScore;
    }

    public void setBlackScore(Score blackScore) {
        this.blackScore = blackScore;
    }

    public Color getWinner() {
        return winner;
    }

    public void setWinner(Color winner) {
        this.winner = winner;
    }
}
