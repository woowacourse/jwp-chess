package chess.service;

import chess.dao.GameRepository;
import chess.dao.ChessBoardDao;
import chess.dao.ChessMemberDao;
import chess.dao.ChessPieceDao;
import chess.dao.ChessPositionDao;
import chess.domain.game.ChessBoardInitializer;
import chess.domain.game.ChessBoard;
import chess.domain.game.BoardInitializer;
import chess.domain.pieces.Color;
import chess.domain.pieces.Piece;
import chess.domain.position.Position;
import chess.dto.response.StatusDto;
import chess.entities.GameEntity;
import chess.entities.PieceEntity;
import chess.entities.PositionEntity;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public final class GameService {

    private final GameRepository gameRepository;
    private final ChessBoardDao boardDao;
    private final ChessPositionDao positionDao;
    private final ChessPieceDao pieceDao;
    private final ChessMemberDao memberDao;

    public GameService(GameRepository gameRepository, ChessBoardDao boardDao, ChessPositionDao positionDao,
                       ChessPieceDao pieceDao, ChessMemberDao memberDao) {
        this.gameRepository = gameRepository;
        this.boardDao = boardDao;
        this.positionDao = positionDao;
        this.pieceDao = pieceDao;
        this.memberDao = memberDao;
    }

    public GameEntity createBoard(final GameEntity board) {
        return saveBoard(board, new ChessBoardInitializer());
    }

    public GameEntity saveBoard(final GameEntity board, final BoardInitializer boardInitializer) {
        final GameEntity savedBoard = boardDao.save(board);
        final Map<Position, Piece> initialize = boardInitializer.initialize();
        positionDao.saveAll(savedBoard.getId());
        for (Position position : initialize.keySet()) {
            int lastPositionId = positionDao.getIdByColumnAndRowAndBoardId(position.getColumn(), position.getRow(),
                    savedBoard.getId());
            final Piece piece = initialize.get(position);
            pieceDao.save(new PieceEntity(piece.getColor(), piece.getType(), lastPositionId));
        }
        memberDao.saveAll(board.getMembers(), savedBoard.getId());
        return savedBoard;
    }

    public void move(final int roomId, final Position sourceRawPosition, final Position targetRawPosition) {
        PositionEntity sourcePosition = positionDao.getByColumnAndRowAndBoardId(sourceRawPosition.getColumn(),
                sourceRawPosition.getRow(), roomId);
        PositionEntity targetPosition = positionDao.getByColumnAndRowAndBoardId(targetRawPosition.getColumn(),
                targetRawPosition.getRow(), roomId);
        ChessBoard chessBoard = new ChessBoard(() -> positionDao.findAllPositionsAndPieces(roomId));
        validateTurn(roomId, sourceRawPosition);
        chessBoard.move(sourceRawPosition, targetRawPosition);
        updateMovingPiecePosition(sourcePosition, targetPosition, chessBoard.piece(targetRawPosition));
        changeTurn(roomId);
    }

    private void validateTurn(final int roomId, final Position sourcePosition) {
        final Optional<Piece> wrappedPiece = pieceDao.findByPositionId(
                positionDao.getIdByColumnAndRowAndBoardId(sourcePosition.getColumn(), sourcePosition.getRow(), roomId));
        wrappedPiece.ifPresent(piece -> validateCorrectTurn(roomId, piece));
    }

    private void validateCorrectTurn(int roomId, Piece piece) {
        final Color turn = gameRepository.getById(roomId).getTurn();
        if (!piece.isSameColor(turn)) {
            throw new IllegalArgumentException("지금은 " + turn.value() + "의 턴입니다.");
        }
    }

    private void updateMovingPiecePosition(PositionEntity sourcePosition, PositionEntity targetPosition,
                                           Optional<Piece> targetPiece) {
        if (targetPiece.isPresent()) {
            pieceDao.deleteByPositionId(targetPosition.getId());
        }
        pieceDao.updatePositionId(sourcePosition.getId(), targetPosition.getId());
    }

    private void changeTurn(int roomId) {
        boardDao.updateTurn(Color.opposite(gameRepository.getById(roomId).getTurn()), roomId);
    }

    public GameEntity getBoard(int roomId) {
        return gameRepository.getById(roomId);
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
        return chessBoard.isEnd();
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

    public List<GameEntity> findAllBoard() {
        return gameRepository.findAll();
    }
}
