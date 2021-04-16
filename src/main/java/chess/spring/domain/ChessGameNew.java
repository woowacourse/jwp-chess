package chess.spring.domain;

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
    private final String currentTurnTeamColor;
    private final double whitePlayerScore;
    private final double blackPlayerScore;

    public ChessGameNew(Long id, String title, String boardStatus, String currentTurnTeamColor, double whitePlayerScore, double blackPlayerScore) {
        this.id = id;
        this.title = title;
        this.boardStatus = boardStatus;
        this.currentTurnTeamColor = currentTurnTeamColor;
        this.whitePlayerScore = whitePlayerScore;
        this.blackPlayerScore = blackPlayerScore;
    }

    public ChessGameNew(String title) {
        this.title = title;
        this.boardStatus = INITIAL_BOARD_STATUS;
        this.currentTurnTeamColor = "white";
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

    public String getCurrentTurnTeamColor() {
        return currentTurnTeamColor;
    }

    public double getWhitePlayerScore() {
        return whitePlayerScore;
    }

    public double getBlackPlayerScore() {
        return blackPlayerScore;
    }
}
