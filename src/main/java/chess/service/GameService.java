package chess.service;

import chess.dao.BoardRepository;
import chess.dao.WebChessBoardDao;
import chess.dao.WebChessMemberDao;
import chess.dao.WebChessPieceDao;
import chess.dao.WebChessPositionDao;
import chess.domain.game.ChessBoardInitializer;
import chess.domain.game.ChessBoard;
import chess.domain.game.BoardInitializer;
import chess.domain.pieces.Color;
import chess.domain.pieces.Piece;
import chess.domain.position.Position;
import chess.dto.response.StatusDto;
import chess.entities.ChessGame;
import chess.entities.ChessPiece;
import chess.entities.ChessPosition;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public final class GameService {

    private final BoardRepository boardRepository;
    private final WebChessBoardDao boardDao;
    private final WebChessPositionDao positionDao;
    private final WebChessPieceDao pieceDao;
    private final WebChessMemberDao memberDao;

    public GameService(BoardRepository boardRepository, WebChessBoardDao boardDao, WebChessPositionDao positionDao,
                       WebChessPieceDao pieceDao, WebChessMemberDao memberDao) {
        this.boardRepository = boardRepository;
        this.boardDao = boardDao;
        this.positionDao = positionDao;
        this.pieceDao = pieceDao;
        this.memberDao = memberDao;
    }

    public ChessGame createBoard(final ChessGame board) {
        return saveBoard(board, new ChessBoardInitializer());
    }

    public ChessGame saveBoard(final ChessGame board, final BoardInitializer boardInitializer) {
        final ChessGame savedBoard = boardDao.save(board);
        final Map<Position, Piece> initialize = boardInitializer.initialize();
        positionDao.saveAll(savedBoard.getId());
        for (Position position : initialize.keySet()) {
            int lastPositionId = positionDao.getIdByColumnAndRowAndBoardId(position.getColumn(), position.getRow(),
                    savedBoard.getId());
            final Piece piece = initialize.get(position);
            pieceDao.save(new ChessPiece(piece.getColor(), piece.getType(), lastPositionId));
        }
        memberDao.saveAll(board.getMembers(), savedBoard.getId());
        return savedBoard;
    }

    public void move(final int roomId, final Position sourceRawPosition, final Position targetRawPosition) {
        ChessPosition sourcePosition = positionDao.getByColumnAndRowAndBoardId(sourceRawPosition.getColumn(),
                sourceRawPosition.getRow(), roomId);
        ChessPosition targetPosition = positionDao.getByColumnAndRowAndBoardId(targetRawPosition.getColumn(),
                targetRawPosition.getRow(), roomId);
        ChessBoard chessBoard = new ChessBoard(() -> positionDao.findAllPositionsAndPieces(roomId));
        chessBoard.validateMovement(sourceRawPosition, targetRawPosition);
        validateTurn(roomId, new Position(sourcePosition.getColumn(), sourcePosition.getRow()));
        updateMovingPiecePosition(sourcePosition, targetPosition, chessBoard.piece(targetRawPosition));
        changeTurn(roomId);
    }

    private void validateTurn(final int roomId, final Position sourcePosition) {
        final Optional<Piece> wrappedPiece = pieceDao.findByPositionId(
                positionDao.getIdByColumnAndRowAndBoardId(sourcePosition.getColumn(), sourcePosition.getRow(), roomId));
        wrappedPiece.ifPresent(piece -> validateCorrectTurn(roomId, piece));
    }

    private void validateCorrectTurn(int roomId, Piece piece) {
        final Color turn = boardRepository.getById(roomId).getTurn();
        if (!piece.isSameColor(turn)) {
            throw new IllegalArgumentException("지금은 " + turn.value() + "의 턴입니다.");
        }
    }

    private void updateMovingPiecePosition(ChessPosition sourcePosition, ChessPosition targetPosition,
                                           Optional<Piece> targetPiece) {
        if (targetPiece.isPresent()) {
            pieceDao.deleteByPositionId(targetPosition.getId());
        }
        pieceDao.updatePositionId(sourcePosition.getId(), targetPosition.getId());
    }

    private void changeTurn(int roomId) {
        boardDao.updateTurn(Color.opposite(boardRepository.getById(roomId).getTurn()), roomId);
    }

    public ChessGame getBoard(int roomId) {
        return boardRepository.getById(roomId);
    }

    public Map<String, Piece> getPieces(int roomId) {
        final Map<Position, Piece> allPositionsAndPieces = positionDao.findAllPositionsAndPieces(roomId);
        return mapPositionToString(allPositionsAndPieces);
    }

    private Map<String, Piece> mapPositionToString(Map<Position, Piece> allPositionsAndPieces) {
        return allPositionsAndPieces.keySet().stream()
                .collect(Collectors.toMap(
                        position -> position.getRow().value() + position.getColumn().name(),
                        allPositionsAndPieces::get));
    }

    public boolean isEnd(int roomId) {
        ChessBoard chessBoard = new ChessBoard(() -> positionDao.findAllPositionsAndPieces(roomId));
        final boolean kingDead = chessBoard.isEnd();
        return kingDead;
    }

    public StatusDto status(int roomId) {
        return new StatusDto(Arrays.stream(Color.values())
                .collect(Collectors.toMap(Enum::name, color -> calculateScore(roomId, color))));
    }

    public double calculateScore(int roomId, final Color color) {
        ChessBoard chessBoard = new ChessBoard(() -> positionDao.findAllPositionsAndPieces(roomId));
        return chessBoard.calculateScore(color);
    }

    public boolean delete(int roomId, String password) {
        return boardDao.deleteByIdAndPassword(roomId, password) == 1;
    }

    public List<ChessGame> findAllBoard() {
        return boardRepository.findAll();
    }
}
