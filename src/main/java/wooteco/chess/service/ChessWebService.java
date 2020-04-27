package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.db.ChessPiece;
import wooteco.chess.db.MoveHistoryDao;
import wooteco.chess.db.PieceDao;
import wooteco.chess.domains.board.Board;
import wooteco.chess.domains.board.BoardFactory;
import wooteco.chess.domains.piece.Piece;
import wooteco.chess.domains.piece.PieceColor;
import wooteco.chess.domains.position.Position;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ChessWebService {
    public static final int BOARD_CELLS_NUMBER = 64;
    private static final String TURN_MSG_FORMAT = "%s의 순서입니다.";
    private static final String WINNING_MSG_FORMAT = "%s이/가 이겼습니다.";
    private static final String PIECE_NOT_FOUND_ERR_MSG = "해당 Piece 정보를 찾을 수 없습니다.";

    private final PieceDao pieceDao;
    private final MoveHistoryDao moveHistoryDao;
    private final Map<String, Board> playingBoards = new HashMap<>();

    public ChessWebService(PieceDao pieceDao, MoveHistoryDao moveHistoryDao) {
        this.pieceDao = pieceDao;
        this.moveHistoryDao = moveHistoryDao;
    }

    public boolean canResume(String gameId) throws SQLException {
        int savedPiecesNumber = pieceDao.countSavedPieces(gameId);
        return savedPiecesNumber == BOARD_CELLS_NUMBER;
    }

    public void startNewGame(String gameId) throws SQLException {
        deleteSavedGame(gameId);

        Board board = new Board();
        playingBoards.put(gameId, board);

        List<ChessPiece> chessPieces = createChessPieces(gameId, board);
        pieceDao.addInitialPieces(chessPieces);
    }

    private List<ChessPiece> createChessPieces(String gameId, Board board) {
        return Position.stream()
                .map(position -> {
                    Piece piece = board.getPieceByPosition(position);
                    return new ChessPiece(gameId, position.name(), piece.name());
                })
                .collect(Collectors.toList());
    }

    public void resumeGame(String gameId) throws SQLException {
        Map<Position, Piece> savedBoardStatus = Position.stream()
                .collect(Collectors.toMap(Function.identity(), position -> {
                    String pieceName = null;
                    try {
                        pieceName = pieceDao.findPieceNameByPosition(gameId, position);
                    } catch (SQLException e) {
                        System.out.println(PIECE_NOT_FOUND_ERR_MSG);
                    }
                    return BoardFactory.findPieceByPieceName(pieceName);
                }));

        Optional<String> lastTurn = moveHistoryDao.figureLastTurn(gameId);

        Board board = new Board();
        board.recoverBoard(savedBoardStatus, lastTurn);

        playingBoards.put(gameId, board);
    }

    public void move(String gameId, String sourceName, String targetName) throws SQLException {
        Board board = playingBoards.get(gameId);
        PieceColor currentTeam = board.getTeamColor();

        Position source = Position.ofPositionName(sourceName);
        Position target = Position.ofPositionName(targetName);

        board.move(source, target);

        pieceDao.updatePiece(gameId, source, board.getPieceByPosition(source));
        pieceDao.updatePiece(gameId, target, board.getPieceByPosition(target));
        moveHistoryDao.addMoveHistory(gameId, currentTeam, source, target);
    }

    public String provideWinner(String gameId) throws SQLException {
        Board board = playingBoards.get(gameId);

        if (board.isGameOver()) {
            deleteSavedGame(gameId);
            return winningMsg(board);
        }
        return "";
    }

    public Map<String, Object> provideGameInfo(String gameId) {
        Board board = playingBoards.get(gameId);

        Map<String, Object> gameInfo = new HashMap<>();
        gameInfo.put("pieces", convertPieces(board));
        gameInfo.put("turn", turnMsg(board));
        gameInfo.put("white_score", calculateScore(board, PieceColor.WHITE));
        gameInfo.put("black_score", calculateScore(board, PieceColor.BLACK));

        return gameInfo;
    }

    private void deleteSavedGame(String gameId) throws SQLException {
        pieceDao.deleteBoardStatus(gameId);
        moveHistoryDao.deleteMoveHistory(gameId);
        playingBoards.remove(gameId);
    }

    private List<String> convertPieces(Board board) {
        List<Piece> pieces = board.showBoard();
        return pieces.stream()
                .map(Piece::symbol)
                .collect(Collectors.toList());
    }

    private String turnMsg(Board board) {
        PieceColor team = board.getTeamColor();
        return String.format(TURN_MSG_FORMAT, team.name());
    }

    private double calculateScore(Board board, PieceColor pieceColor) {
        return board.calculateScore(pieceColor);
    }

    private String winningMsg(Board board) {
        PieceColor winner = board.getTeamColor().changeTeam();
        return String.format(WINNING_MSG_FORMAT, winner);
    }
}
