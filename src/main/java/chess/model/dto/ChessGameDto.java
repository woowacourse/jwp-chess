package chess.model.dto;

import chess.model.domain.board.ChessGame;
import chess.model.domain.board.Square;
import chess.model.domain.board.TeamScore;
import chess.model.domain.piece.Bishop;
import chess.model.domain.piece.King;
import chess.model.domain.piece.Knight;
import chess.model.domain.piece.Pawn;
import chess.model.domain.piece.Piece;
import chess.model.domain.piece.Queen;
import chess.model.domain.piece.Rook;
import chess.model.domain.piece.Team;
import chess.model.domain.state.MoveState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import util.NullChecker;

public class ChessGameDto {

    private static final Map<Piece, String> PIECES_LETTER;
    private static final Map<Team, String> DEFAULT_NAMES;

    static {
        Map<Piece, String> piecesLetter = new HashMap<>();
        piecesLetter.put(Pawn.getInstance(Team.BLACK), "♟");
        piecesLetter.put(Pawn.getInstance(Team.WHITE), "♙");
        piecesLetter.put(Rook.getPieceInstance(Team.BLACK), "♜");
        piecesLetter.put(Rook.getPieceInstance(Team.WHITE), "♖");
        piecesLetter.put(Knight.getPieceInstance(Team.BLACK), "♞");
        piecesLetter.put(Knight.getPieceInstance(Team.WHITE), "♘");
        piecesLetter.put(Bishop.getPieceInstance(Team.BLACK), "♝");
        piecesLetter.put(Bishop.getPieceInstance(Team.WHITE), "♗");
        piecesLetter.put(Queen.getPieceInstance(Team.BLACK), "♛");
        piecesLetter.put(Queen.getPieceInstance(Team.WHITE), "♕");
        piecesLetter.put(King.getPieceInstance(Team.BLACK), "♚");
        piecesLetter.put(King.getPieceInstance(Team.WHITE), "♔");
        PIECES_LETTER = Collections.unmodifiableMap(piecesLetter);

        Map<Team, String> defaultNames = new HashMap<>();
        defaultNames.put(Team.BLACK, "BLACK");
        defaultNames.put(Team.WHITE, "WHITE");
        DEFAULT_NAMES = Collections.unmodifiableMap(defaultNames);
    }

    private final List<String> pieces;
    private final String turn;
    private final String state;
    private final String blackScore;
    private final String whiteScore;
    private final String blackName;
    private final String whiteName;
    private final String winner;

    public ChessGameDto(ChessGame chessGame, MoveState moveState, TeamScore teamScore,
        Map<Team, String> names) {
        NullChecker.validateNotNull(chessGame, moveState);
        Map<Square, Piece> board = chessGame.getChessBoard();
        List<String> pieces = new ArrayList<>();
        for (int rank = Square.MAX_FILE_AND_RANK_COUNT;
            rank >= Square.MIN_FILE_AND_RANK_COUNT; rank--) {
            printRankRaw(pieces, board, rank);
        }
        this.pieces = pieces;
        this.turn = chessGame.getGameTurn().getName();
        this.state = moveState.getMessage();
        this.blackScore = String.valueOf(teamScore.get(Team.BLACK));
        this.whiteScore = String.valueOf(teamScore.get(Team.WHITE));
        this.blackName = names.get(Team.BLACK);
        this.whiteName = names.get(Team.WHITE);
        this.winner = teamScore.getWinners().stream()
            .map(names::get)
            .collect(Collectors.joining(", "));
    }

    public ChessGameDto(ChessGame chessGame, Map<Team, String> names) {
        this(chessGame, MoveState.EMPTY, chessGame.getTeamScore(), names);
    }

    public ChessGameDto(TeamScore teamScore, Map<Team, String> names) {
        this.pieces = IntStream.rangeClosed(Square.MIN_FILE_AND_RANK_COUNT,
            Square.MAX_FILE_AND_RANK_COUNT * Square.MAX_FILE_AND_RANK_COUNT)
            .mapToObj(number -> "")
            .collect(Collectors.toList());
        this.turn = null;
        this.state = null;
        this.blackScore = String.valueOf(teamScore.get(Team.BLACK));
        this.whiteScore = String.valueOf(teamScore.get(Team.WHITE));
        this.blackName = names.get(Team.BLACK);
        this.whiteName = names.get(Team.WHITE);
        this.winner = teamScore.getWinners().stream()
            .map(names::get)
            .collect(Collectors.joining(", "));
    }

    private static void printRankRaw(List<String> pieces, Map<Square, Piece> board, int rank) {
        for (char file = 'a'; file <= 'h'; file++) {
            pieces.add(getLetterByFileColumn(board, rank, file));
        }
    }

    private static String getLetterByFileColumn(Map<Square, Piece> gameBoard, int rank,
        char file) {
        if (gameBoard.containsKey(Square.of(String.valueOf(file) + rank))) {
            return PIECES_LETTER.get(gameBoard.get(Square.of(String.valueOf(file) + rank)));
        }
        return "";
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
