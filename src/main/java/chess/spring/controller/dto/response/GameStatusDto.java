package chess.spring.controller.dto.response;

import chess.spring.domain.ChessGameNew;
import java.util.ArrayList;
import java.util.List;

public class GameStatusDto {
    private final Long gameId;
    private final String title;
    private final String beforeTurnTeamColorName;
    private final String currentTurnTeamColorName;
    private final String boardStatus;
    private final double whitePlayerScore;
    private final double blackPlayerScore;
    private final boolean isKingDead;

    public GameStatusDto(ChessGameNew chessGame) {
        this.gameId = chessGame.getId();
        this.title = chessGame.getTitle();
        this.beforeTurnTeamColorName = chessGame.getCurrentTurnTeamColor().oppositeTeamColorName();
        this.currentTurnTeamColorName = chessGame.getCurrentTurnTeamColor().getName();
        this.boardStatus = chessGame.getBoardStatus();
        this.whitePlayerScore = chessGame.getWhitePlayerScore();
        this.blackPlayerScore = chessGame.getBlackPlayerScore();
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
