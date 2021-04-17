package chess.domain.game;

import chess.domain.board.Board;
import chess.domain.board.move.MoveRequest;
import chess.domain.board.score.Scores;
import chess.domain.color.type.TeamColor;
import chess.domain.position.Position;

public class ChessGame {

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
    private static final String INITIAL_TURN_TEAM_COLOR_VALUE = "white";

    private final Long id;
    private final String title;
    private String boardStatus;
    private TeamColor currentTurnTeamColor;
    private final Board board;

    public ChessGame(Long id, String title, String boardStatus, String currentTurnTeamColor, double whitePlayerScore, double blackPlayerScore) {
        this.id = id;
        this.title = title;
        this.boardStatus = boardStatus;
        this.currentTurnTeamColor = TeamColor.of(currentTurnTeamColor);
        this.board = new Board(boardStatus);
    }

    public ChessGame(String title) {
        this(null, title, INITIAL_BOARD_STATUS, INITIAL_TURN_TEAM_COLOR_VALUE, INITIAL_SCORE, INITIAL_SCORE);
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

    public void movePiece(Position startPosition, Position destination) {
        MoveRequest moveRequest = new MoveRequest(currentTurnTeamColor, startPosition, destination);
        board.movePiece(moveRequest);
        boardStatus = board.getBoardStatus();
        currentTurnTeamColor = currentTurnTeamColor.oppositeTeamColor();
    }

    public TeamColor getCurrentTurnTeamColor() {
        return currentTurnTeamColor;
    }

    public boolean isKingDead() {
        return board.isKingDead();
    }

    public Scores getScores() {
        return board.getScores();
    }

    public String getCurrentTurnTeamColorValue() {
        return currentTurnTeamColor.getValue();
    }

    public String getCurrentTurnTeamColorName() {
        return currentTurnTeamColor.getName();
    }

    public String getOppositeTurnTeamColorName() {
        return currentTurnTeamColor.getOppositeTeamColorName();
    }
}
