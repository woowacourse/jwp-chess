package chess.service;

import chess.dao.ChessBoardDao;
import chess.dao.ChessMemberDao;
import chess.dao.ChessPieceDao;
import chess.dao.ChessPositionDao;
import chess.dao.GameRepository;
import chess.domain.game.ChessBoard;
import chess.domain.game.ChessBoardInitializer;
import chess.domain.pieces.Color;
import chess.domain.pieces.Piece;
import chess.domain.position.Position;
import chess.dto.response.StatusDto;
import chess.entities.GameEntity;
import chess.entities.PieceEntity;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
        final GameEntity savedBoard = boardDao.save(board);
        final Map<Position, Piece> initialize = new ChessBoardInitializer().initialize();
        positionDao.saveAll(savedBoard.getId());
        for (Position position : initialize.keySet()) {
            int lastPositionId = positionDao.getIdByColumnAndRowAndBoardId(position.getColumn(), position.getRow(),
                    savedBoard.getId());
            final Piece piece = initialize.get(position);
            pieceDao.save(new PieceEntity(piece.getColor(), piece.getType(), lastPositionId));
        }
        memberDao.saveAll(board.getMemberEntities(), savedBoard.getId());
        return savedBoard;
    }

    public void move(final int roomId, final Position sourceRawPosition, final Position targetRawPosition) {
        GameEntity gameEntity = gameRepository.getById(roomId);
        gameEntity.move(sourceRawPosition, targetRawPosition);
        gameRepository.update(gameEntity, sourceRawPosition, targetRawPosition);
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

    private double calculateScore(int roomId, final Color color) {
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
