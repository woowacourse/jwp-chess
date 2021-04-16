package chess.spring.domain;

import chess.web.domain.player.type.TeamColor;

public class ChessGameNew {

    private static final String INITIAL_BOARD_STATUS = ""
        + "RNBQKBNR"
        + "PPPPPPPP"
        + "........"
        + "........"
        + "........"
        + "........"
        + "pppppppp"
        + "rnbqkbnr";
    private static final double INITIAL_SCORE = 38.0;

    private Long id;
    private final String title;
    private final String boardStatus;
    private final TeamColor currentTurnTeamColor;
    private final double whitePlayerScore;
    private final double blackPlayerScore;

    public ChessGameNew(Long id, String title, String boardStatus, String currentTurnTeamColor, double whitePlayerScore, double blackPlayerScore) {
        this.id = id;
        this.title = title;
        this.boardStatus = boardStatus;
        this.currentTurnTeamColor = TeamColor.of(currentTurnTeamColor);
        this.whitePlayerScore = whitePlayerScore;
        this.blackPlayerScore = blackPlayerScore;
    }

    public ChessGameNew(String title) {
        this.title = title;
        this.boardStatus = INITIAL_BOARD_STATUS;
        this.currentTurnTeamColor = TeamColor.WHITE;
        this.whitePlayerScore = INITIAL_SCORE;
        this.blackPlayerScore = INITIAL_SCORE;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBoardStatus() {
        return boardStatus;
    }

    public TeamColor getCurrentTurnTeamColor() {
        return currentTurnTeamColor;
    }

    public double getWhitePlayerScore() {
        return whitePlayerScore;
    }

    public double getBlackPlayerScore() {
        return blackPlayerScore;
    }

    public boolean isKingDead() {
        return false;
    }
}
