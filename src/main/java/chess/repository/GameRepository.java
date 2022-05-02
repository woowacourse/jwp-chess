package chess.repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import chess.database.GameStateGenerator;
import chess.database.PieceCache;
import chess.database.dao.GameDao;
import chess.database.dao.PieceDao;
import chess.database.entity.GameEntity;
import chess.database.entity.PieceEntity;
import chess.database.entity.PointEntity;
import chess.domain.board.Board;
import chess.domain.board.CustomBoardGenerator;
import chess.domain.board.Point;
import chess.domain.board.Route;
import chess.domain.game.GameState;
import chess.domain.piece.Piece;

@Repository
public class GameRepository {

    private final GameDao gameDao;
    private final PieceDao pieceDao;

    public GameRepository(GameDao gameDao, PieceDao pieceDao) {
        this.gameDao = gameDao;
        this.pieceDao = pieceDao;
    }

    public void updateState(GameState state, Long gameId) {
        final GameEntity entity = GameEntity.from(state, gameId);
        gameDao.updateGame(entity);
    }

    public void updateState(GameState state, Route route, Long gameId) {
        updateState(state, gameId);
        pieceDao.deletePiece(PointEntity.from(route.getDestination()), gameId);
        pieceDao.updatePiece(PointEntity.from(route.getSource()), PointEntity.from(route.getDestination()), gameId);
    }

    public GameState findGameById(Long gameId) {
        return toGameState(findGameOrThrow(gameId), findBoardOrThrow(gameId));
    }

    private GameState toGameState(GameEntity gameEntity, List<PieceEntity> pieceEntities) {
        final Board board = Board.of(new CustomBoardGenerator(toPointPieces(pieceEntities)));
        return GameStateGenerator.generate(board, gameEntity.getState(), gameEntity.getTurnColor());
    }

    private List<PieceEntity> findBoardOrThrow(Long gameId) {
        final List<PieceEntity> pieceEntities = pieceDao.findBoardByGameId(gameId);
        if (pieceEntities == null || pieceEntities.isEmpty()) {
            throw new IllegalArgumentException(String.format("[ERROR] %d 게임 번호에 해당하는 보드가 없습니다.", gameId));
        }
        return pieceEntities;
    }

    private GameEntity findGameOrThrow(Long gameId) {
        return gameDao.findGameById(gameId)
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("[ERROR] %d 게임 번호에 해당하는 게임이 없습니다.", gameId)
            ));
    }

    private Map<Point, Piece> toPointPieces(List<PieceEntity> pieceEntities) {
        return pieceEntities.stream().collect(Collectors.toMap(
            pieceEntity -> Point.of(pieceEntity.getHorizontalIndex(), pieceEntity.getVerticalIndex()),
            pieceEntity -> PieceCache.getPiece(pieceEntity.getPieceType(), pieceEntity.getPieceColor())
        ));
    }

    public GameState findGameByRoomId(Long roomId) {
        final GameEntity gameEntity = findGameByRoomIdOrThrow(roomId);
        List<PieceEntity> pieceEntities = findBoardOrThrow(gameEntity.getId());
        return toGameState(gameEntity, pieceEntities);
    }

    private GameEntity findGameByRoomIdOrThrow(Long roomId) {
        return gameDao.findGameByRoomId(roomId)
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("[ERROR] %d 방 번호에 해당하는 게임이 없습니다.", roomId)
            ));
    }

    public Long findGameIdByRoomId(Long roomId) {
        final GameEntity entity = findGameByRoomIdOrThrow(roomId);
        return entity.getId();
    }
}
