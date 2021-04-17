package chess.controller.dto.response;

import chess.domain.board.score.Scores;
import chess.domain.game.ChessGame;

public class GameStatusResponseDto {

    private final Long gameId;
    private final String title;
    private final String beforeTurnTeamColorName;
    private final String currentTurnTeamColorName;
    private final String boardStatus;
    private final double whitePlayerScore;
    private final double blackPlayerScore;
    private final boolean isKingDead;

    public GameStatusResponseDto(ChessGame chessGame) {
        this.gameId = chessGame.getId();
        this.title = chessGame.getTitle();
        this.beforeTurnTeamColorName = chessGame.getOppositeTurnTeamColorName();
        this.currentTurnTeamColorName = chessGame.getCurrentTurnTeamColorName();
        this.boardStatus = chessGame.getBoardStatus();
        Scores scores = chessGame.getScores();
        this.whitePlayerScore = scores.getWhitePlayerScore();
        this.blackPlayerScore = scores.getBlackPlayerScore();
        this.isKingDead = chessGame.isKingDead();
    }

    public Long getGameId() {
        return gameId;
    }

    public String getTitle() {
        return title;
    }

    public String getBeforeTurnTeamColorName() {
        return beforeTurnTeamColorName;
    }

    public String getCurrentTurnTeamColorName() {
        return currentTurnTeamColorName;
    }

    public String getBoardStatus() {
        return boardStatus;
    }

    public double getWhitePlayerScore() {
        return whitePlayerScore;
    }

    public double getBlackPlayerScore() {
        return blackPlayerScore;
    }

    public boolean isKingDead() {
        return isKingDead;
    }
}
