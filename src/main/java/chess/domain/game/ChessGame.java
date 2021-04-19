package chess.domain.game;

import chess.domain.board.Board;
import chess.domain.board.move.MoveRequest;
import chess.domain.board.score.Scores;
import chess.domain.color.type.TeamColor;
import chess.domain.password.PasswordEncoder;

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

    private static final String INITIAL_TURN_TEAM_COLOR_VALUE = "white";

    private final Long id;
    private final String title;
    private String boardStatus;
    private TeamColor currentTurnTeamColor;
    private final Board board;
    private final String encryptedWhitePlayerPassword;
    private String encryptedBlackPlayerPassword;

    public ChessGame(Long id, String title, String boardStatus, String currentTurnTeamColor, String encryptedWhitePlayerPassword, String encryptedBlackPlayerPassword) {
        this.id = id;
        this.title = title;
        this.boardStatus = boardStatus;
        this.currentTurnTeamColor = TeamColor.of(currentTurnTeamColor);
        this.board = new Board(boardStatus);
        this.encryptedWhitePlayerPassword = encryptedWhitePlayerPassword;
        this.encryptedBlackPlayerPassword = encryptedBlackPlayerPassword;
    }

    public ChessGame(String title, String rawWhitePlayerPassword) {
        this(null, title, INITIAL_BOARD_STATUS, INITIAL_TURN_TEAM_COLOR_VALUE, PasswordEncoder.encrypt(rawWhitePlayerPassword), null);
    }
    public ChessGame(String title, String encryptedWhitePlayerPassword, String encryptedBlackPlayerPassword) {
        this(null, title, INITIAL_BOARD_STATUS, INITIAL_TURN_TEAM_COLOR_VALUE, encryptedWhitePlayerPassword, encryptedBlackPlayerPassword);
    }

    public ChessGame(String title) {
        this(null, title, INITIAL_BOARD_STATUS, INITIAL_TURN_TEAM_COLOR_VALUE, null, null);
    }

    public ChessGame() {
        this(null, "title", INITIAL_BOARD_STATUS, INITIAL_TURN_TEAM_COLOR_VALUE, null, null);
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

    public void movePiece(String startPositionInput, String destinationInput) {
        MoveRequest moveRequest = new MoveRequest(currentTurnTeamColor, startPositionInput, destinationInput);
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

    public String getEncryptedWhitePlayerPassword() {
        return encryptedWhitePlayerPassword;
    }

    public String getEncryptedBlackPlayerPassword() {
        return encryptedBlackPlayerPassword;
    }

    public void joinBlackPlayerWithPassword(String rawBlackPlayerPassword) {
        encryptedBlackPlayerPassword = PasswordEncoder.encrypt(rawBlackPlayerPassword);
    }
}
