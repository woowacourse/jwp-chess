package chess.domain.entity;

import chess.domain.manager.GameStatus;

public class Score implements Entity<Long> {

    private Long id;
    private Long gameId;
    private double whiteScore;
    private double blackScore;

    public Score() {
    }

    public Score(Long id, Long gameId, double whiteScore, double blackScore) {
        this.id = id;
        this.gameId = gameId;
        this.whiteScore = whiteScore;
        this.blackScore = blackScore;
    }

    public Score(Long gameId, double whiteScore, double blackScore) {
        this.gameId = gameId;
        this.whiteScore = whiteScore;
        this.blackScore = blackScore;
    }

    public static Score of(GameStatus gameStatus, Long gameId) {
        return new Score(gameId, gameStatus.whiteScore(), gameStatus.blackScore());
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public double getWhiteScore() {
        return whiteScore;
    }

    public void setWhiteScore(double whiteScore) {
        this.whiteScore = whiteScore;
    }

    public double getBlackScore() {
        return blackScore;
    }

    public void setBlackScore(double blackScore) {
        this.blackScore = blackScore;
    }
}
