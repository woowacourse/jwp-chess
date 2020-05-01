package chess.dto.view;

import chess.model.domain.board.ChessGame;
import chess.model.domain.board.Square;
import chess.model.domain.board.TeamScore;
import chess.model.domain.piece.Piece;
import chess.model.domain.piece.Team;
import chess.model.domain.state.MoveState;
import chess.util.PieceLetterConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameInformationDto {

    private static final List<String> EMPTY_PIECES;

    static {
        EMPTY_PIECES = Collections
            .unmodifiableList(IntStream.rangeClosed(Square.MIN_FILE_AND_RANK_COUNT,
                Square.MAX_FILE_AND_RANK_COUNT * Square.MAX_FILE_AND_RANK_COUNT)
                .mapToObj(number -> "")
                .collect(Collectors.toList()));
    }

    private List<String> pieces = EMPTY_PIECES;
    private String turn = null;
    private String state = MoveState.EMPTY.getMessage();
    private String blackScore = null;
    private String whiteScore = null;
    private String blackName = null;
    private String whiteName = null;
    private String winner = null;

    public GameInformationDto(Map<Team, String> userNames) {
        if (!userNames.isEmpty()) {
            blackName(userNames.get(Team.BLACK));
            whiteName(userNames.get(Team.WHITE));
        }
    }

    public GameInformationDto chessGame(ChessGame chessGame) {
        pieces(chessGame.getChessBoard());
        turn(chessGame.getTurn());
        TeamScore teamScore = chessGame.deriveTeamScore();
        teamScore(teamScore);
        return this;
    }

    public GameInformationDto pieces(Map<Square, Piece> chessBoard) {
        List<String> pieces = new ArrayList<>();
        for (int rank = Square.MAX_FILE_AND_RANK_COUNT; rank >= Square.MIN_FILE_AND_RANK_COUNT;
            rank--) {
            printRankRaw(pieces, chessBoard, rank);
        }
        this.pieces = pieces;
        return this;
    }

    public GameInformationDto turn(Team turn) {
        this.turn = turn.getName();
        return this;
    }

    public GameInformationDto moveState(MoveState moveState) {
        this.state = moveState.getMessage();
        return this;
    }

    public GameInformationDto teamScore(TeamScore teamScore) {
        blackScore(teamScore.get(Team.BLACK));
        whiteScore(teamScore.get(Team.WHITE));
        winner(teamScore.getWinners());
        return this;
    }

    public GameInformationDto winner(List<Team> winners) {
        this.winner = winners.stream()
            .map(team -> makeUserNames().get(team))
            .collect(Collectors.joining(", "));
        return this;
    }

    public GameInformationDto blackScore(Double blackScore) {
        this.blackScore = String.valueOf(blackScore);
        return this;
    }

    public GameInformationDto whiteScore(Double whiteScore) {
        this.whiteScore = String.valueOf(whiteScore);
        return this;
    }

    public GameInformationDto blackName(String blackName) {
        this.blackName = blackName;
        return this;
    }

    public GameInformationDto whiteName(String whiteName) {
        this.whiteName = whiteName;
        return this;
    }

    private static void printRankRaw(List<String> pieces, Map<Square, Piece> board, int rank) {
        for (char file = 'a'; file <= 'h'; file++) {
            pieces.add(getLetterByFileColumn(board, rank, file));
        }
    }

    private static String getLetterByFileColumn(Map<Square, Piece> gameBoard, int rank,
        char file) {
        if (gameBoard.containsKey(Square.of(String.valueOf(file) + rank))) {
            return PieceLetterConverter
                .convertToLetter(gameBoard.get(Square.of(String.valueOf(file) + rank)));
        }
        return "";
    }

    public TeamScore makeTeamScore() {
        Map<Team, Double> teamScore = new HashMap<>();
        teamScore.put(Team.BLACK, Double.valueOf(blackScore));
        teamScore.put(Team.WHITE, Double.valueOf(whiteScore));
        return new TeamScore(teamScore);
    }

    public Map<Team, String> makeUserNames() {
        Map<Team, String> userNames = new HashMap<>();
        userNames.put(Team.BLACK, blackName);
        userNames.put(Team.WHITE, whiteName);
        return userNames;
    }

    public String getTurn() {
        return turn;
    }

    public String getState() {
        return state;
    }

    public List<String> getPieces() {
        return pieces;
    }

    public String getBlackScore() {
        return blackScore;
    }

    public String getWhiteScore() {
        return whiteScore;
    }

    public String getBlackName() {
        return blackName;
    }

    public String getWhiteName() {
        return whiteName;
    }

    public String getWinner() {
        return winner;
    }

    @Override
    public String toString() {
        return "ChessGameDto{" +
            "pieces=" + pieces +
            ", turn='" + turn + '\'' +
            ", state='" + state + '\'' +
            ", blackScore='" + blackScore + '\'' +
            ", whiteScore='" + whiteScore + '\'' +
            ", blackName='" + blackName + '\'' +
            ", whiteName='" + whiteName + '\'' +
            ", winner='" + winner + '\'' +
            '}';
    }
}
