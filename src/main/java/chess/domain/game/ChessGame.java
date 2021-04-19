package chess.domain.game;

import chess.domain.board.Board;
import chess.domain.board.move.MoveRequest;
import chess.domain.board.score.Scores;
import chess.domain.color.type.TeamColor;
import chess.domain.password.PasswordEncoder;
import chess.web.controller.dto.request.CreateGameRequestDto;

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
    private static final String NOT_CORRECT_PASSWORD_ERROR_MESSAGE = "비밀번호가 일치하지 않습니다.";

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

    public ChessGame(String title, String encryptedWhitePlayerPassword, String encryptedBlackPlayerPassword) {
        this(null, title, INITIAL_BOARD_STATUS, INITIAL_TURN_TEAM_COLOR_VALUE, encryptedWhitePlayerPassword, encryptedBlackPlayerPassword);
    }
    public ChessGame(String title, TeamColor currentTurnTeamColor, String encryptedWhitePlayerPassword, String encryptedBlackPlayerPassword) {
        this(null, title, INITIAL_BOARD_STATUS, currentTurnTeamColor.getValue(), encryptedWhitePlayerPassword, encryptedBlackPlayerPassword);
    }

    public ChessGame(String title, String rawWhitePlayerPassword) {
        this(null, title, INITIAL_BOARD_STATUS, INITIAL_TURN_TEAM_COLOR_VALUE, PasswordEncoder.encrypt(rawWhitePlayerPassword), null);
    }

    public ChessGame(String title) {
        this(null, title, INITIAL_BOARD_STATUS, INITIAL_TURN_TEAM_COLOR_VALUE, null, null);
    }

    public ChessGame() {
        this(null, "title", INITIAL_BOARD_STATUS, INITIAL_TURN_TEAM_COLOR_VALUE, null, null);
    }

    public ChessGame(CreateGameRequestDto createGameRequestDto) {
        this(createGameRequestDto.getGameTitle(), createGameRequestDto.getRawWhitePlayerPassword());
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

    public void validatePassword(String encryptedPassword) {
        if (currentTurnTeamColor == TeamColor.WHITE) {
            validateWhitePlayerPassword(encryptedPassword);
            return;
        }
        validateBlackPlayerPassword(encryptedPassword);
    }

    private void validateWhitePlayerPassword(String encryptedPassword) {
        if (!encryptedWhitePlayerPassword.equals(encryptedPassword)) {
            throw new IllegalArgumentException(NOT_CORRECT_PASSWORD_ERROR_MESSAGE);
        }
    }

    private void validateBlackPlayerPassword(String encryptedPassword) {
        if (!encryptedBlackPlayerPassword.equals(encryptedPassword)) {
            throw new IllegalArgumentException(NOT_CORRECT_PASSWORD_ERROR_MESSAGE);
        }
    }
}
