package wooteco.chess.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.chess.db.ChessPiece;
import wooteco.chess.db.ChessPieceRepository;
import wooteco.chess.db.MoveHistoryRepository;
import wooteco.chess.domains.board.Board;
import wooteco.chess.domains.board.BoardFactory;
import wooteco.chess.domains.piece.Piece;
import wooteco.chess.domains.piece.PieceColor;
import wooteco.chess.domains.position.Position;

@Service
public class SpringChessService {
    public static final int BOARD_CELLS_NUMBER = 64;
    private static final String TURN_MSG_FORMAT = "%s의 순서입니다.";
    private static final String WINNING_MSG_FORMAT = "%s이/가 이겼습니다.";

    private final ChessPieceRepository chessPieceRepository;
    private final MoveHistoryRepository moveHistoryRepository;
    private final Map<String, Board> playingBoards = new HashMap<>();

    public SpringChessService(ChessPieceRepository chessPieceRepository, MoveHistoryRepository moveHistoryRepository) {
        this.chessPieceRepository = chessPieceRepository;
        this.moveHistoryRepository = moveHistoryRepository;
    }

    public boolean canResume(String gameId) {
        int savedPiecesNumber = chessPieceRepository.countSavedPieces(gameId);
        return savedPiecesNumber == BOARD_CELLS_NUMBER;
    }

    public void startNewGame(String gameId) {
        deleteSavedGame(gameId);

        Board board = new Board();
        playingBoards.put(gameId, board);

        List<ChessPiece> chessPieces = createChessPieces(gameId, board);
        for (ChessPiece chessPiece : chessPieces) {
            chessPieceRepository.savePiece(gameId, chessPiece.getPosition(), chessPiece.getPiece());
        }
    }

    private List<ChessPiece> createChessPieces(String gameId, Board board) {
        return Position.stream()
                .map(position -> {
                    Piece piece = board.getPieceByPosition(position);
                    return new ChessPiece(gameId, position.name(), piece.name());
                })
                .collect(Collectors.toList());
    }

    public void resumeGame(String gameId) {
        Map<Position, Piece> savedBoardStatus = Position.stream()
                .collect(Collectors.toMap(Function.identity(), position -> {
                    String pieceName = null;
                    pieceName = chessPieceRepository.findPieceNameByPosition(gameId, position.name());
                    return BoardFactory.findPieceByPieceName(pieceName);
                }));

        Optional<String> lastTurn = moveHistoryRepository.findLastTurn(gameId);

        Board board = new Board();
        board.recoverBoard(savedBoardStatus, lastTurn);

        playingBoards.put(gameId, board);
    }

    public void move(String gameId, String sourceName, String targetName) {
        Board board = playingBoards.get(gameId);
        PieceColor currentTeam = board.getTeamColor();

        Position source = Position.ofPositionName(sourceName);
        Position target = Position.ofPositionName(targetName);

        board.move(source, target);

        chessPieceRepository.updatePiece(gameId, source.name(), board.getPieceByPosition(source).name());
        chessPieceRepository.updatePiece(gameId, target.name(), board.getPieceByPosition(target).name());
        moveHistoryRepository.addMoveHistory(gameId, currentTeam.name(), source.name(), target.name());
    }

    public String provideWinner(String gameId) {
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

    private void deleteSavedGame(String gameId) {
        chessPieceRepository.deleteById(gameId);
        moveHistoryRepository.deleteById(gameId);
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

    public List<String> findRoomNames() {
        return chessPieceRepository.findRoomNames();
    }
}
