package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.db.ChessPiece;
import wooteco.chess.db.ChessPieceRepository;
import wooteco.chess.db.MoveHistoryRepository;
import wooteco.chess.domains.board.Board;
import wooteco.chess.domains.board.BoardFactory;
import wooteco.chess.domains.piece.Piece;
import wooteco.chess.domains.piece.PieceColor;
import wooteco.chess.domains.position.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// Todo: Move시 자신의 말 위치로 이동하면 IllgalStateException -> 체스 게임 커스텀 exception 생성하고 처리해주기~
@Service
public class SpringChessService {
    public static final int BOARD_CELLS_NUMBER = 64;
    private static final String TURN_MSG_FORMAT = "%s의 순서입니다.";
    private static final String WINNING_MSG_FORMAT = "%s이/가 이겼습니다.";
    private static final String CAN_NOT_RESUME_ERR_MSG = "해당 게임을 이어할 수 없습니다.";

    private final ChessPieceRepository chessPieceRepository;
    private final MoveHistoryRepository moveHistoryRepository;

    public SpringChessService(ChessPieceRepository chessPieceRepository, MoveHistoryRepository moveHistoryRepository) {
        this.chessPieceRepository = chessPieceRepository;
        this.moveHistoryRepository = moveHistoryRepository;
    }

    public List<String> findRooms() {
        return chessPieceRepository.findRooms();
    }

    // Todo: saveAll로 저장
    public void startNewGame(String gameId) {
        deleteSavedGame(gameId);

        Board board = new Board();
        List<ChessPiece> chessPieces = createChessPieces(gameId, board);
        for (ChessPiece chessPiece : chessPieces) {
            chessPieceRepository.savePiece(chessPiece.getGameId(), chessPiece.getPosition(), chessPiece.getPiece());
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
        if (!canResume(gameId)) {
            throw new IllegalArgumentException(CAN_NOT_RESUME_ERR_MSG);
        }

        Board board = new Board();
        List<ChessPiece> chessPieces = chessPieceRepository.findByGameId(gameId);
        Optional<String> lastTurn = moveHistoryRepository.findLastTurn(gameId);

        Map<Position, Piece> savedBoard = chessPieces.stream()
                .collect(Collectors.toMap(
                        piece -> Position.ofPositionName(piece.getPosition()),
                        piece -> BoardFactory.findPieceByPieceName(piece.getPiece())
                ));
        board.recoverBoard(savedBoard, lastTurn);
    }

    private boolean canResume(String gameId) {
        int savedPiecesNumber = chessPieceRepository.countSavedPieces(gameId);
        return savedPiecesNumber == BOARD_CELLS_NUMBER;
    }

    // Todo: 받아온 보드의 piece로 save
    public void move(String gameId, String sourceName, String targetName) {
        Board board = new Board();
        List<ChessPiece> chessPieces = chessPieceRepository.findByGameId(gameId);
        Optional<String> lastTurn = moveHistoryRepository.findLastTurn(gameId);

        Map<Position, Piece> savedBoard = chessPieces.stream()
                .collect(Collectors.toMap(
                        piece -> Position.ofPositionName(piece.getPosition()),
                        piece -> BoardFactory.findPieceByPieceName(piece.getPiece())
                ));
        board.recoverBoard(savedBoard, lastTurn);

        PieceColor currentTeam = board.getTeamColor();

        Position source = Position.ofPositionName(sourceName);
        Position target = Position.ofPositionName(targetName);

        board.move(source, target);

        chessPieceRepository.updatePiece(gameId, source.name(), board.getPieceByPosition(source).name());
        chessPieceRepository.updatePiece(gameId, target.name(), board.getPieceByPosition(target).name());
        moveHistoryRepository.addMoveHistory(gameId, currentTeam.name(), source.name(), target.name());
    }

    public String provideWinner(String gameId) {
        Board board = new Board();
        List<ChessPiece> chessPieces = chessPieceRepository.findByGameId(gameId);
        Optional<String> lastTurn = moveHistoryRepository.findLastTurn(gameId);

        Map<Position, Piece> savedBoard = chessPieces.stream()
                .collect(Collectors.toMap(
                        piece -> Position.ofPositionName(piece.getPosition()),
                        piece -> BoardFactory.findPieceByPieceName(piece.getPiece())
                ));
        board.recoverBoard(savedBoard, lastTurn);

        if (board.isGameOver()) {
            deleteSavedGame(gameId);
            return winningMsg(board);
        }
        return "";
    }

    // Todo: piece을 symbol로 저장? -> convertPieces 삭제, recoverBoard 메서드 사용하지 않고, 보드 생성하지 않고 진행하게

    public Map<String, Object> provideGameInfo(String gameId) {
        Board board = new Board();
        List<ChessPiece> chessPieces = chessPieceRepository.findByGameId(gameId);
        Optional<String> lastTurn = moveHistoryRepository.findLastTurn(gameId);

        Map<Position, Piece> savedBoard = chessPieces.stream()
                .collect(Collectors.toMap(
                        piece -> Position.ofPositionName(piece.getPosition()),
                        piece -> BoardFactory.findPieceByPieceName(piece.getPiece())
                ));
        board.recoverBoard(savedBoard, lastTurn);

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
