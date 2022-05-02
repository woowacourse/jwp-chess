package chess.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import chess.database.dao.GameDao;
import chess.database.dao.PieceDao;
import chess.database.dao.RoomDao;
import chess.database.entity.GameEntity;
import chess.database.entity.PieceEntity;
import chess.database.entity.RoomEntity;
import chess.domain.Room;
import chess.domain.game.GameState;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;

@Repository
public class RoomRepository {
    private final RoomDao roomDao;
    private final GameDao gameDao;
    private final PieceDao pieceDao;

    public RoomRepository(RoomDao roomDao, GameDao gameDao, PieceDao pieceDao) {
        this.roomDao = roomDao;
        this.gameDao = gameDao;
        this.pieceDao = pieceDao;
    }

    public Long createRoom(Room room, GameState gameState) {
        final Long roomId = roomDao.saveRoom(RoomEntity.from(room.getRoomName(), room.getPassword()));
        final Long gameId = gameDao.saveGame(GameEntity.fromRoomId(gameState, roomId));
        pieceDao.saveBoard(toPieceEntities(gameState, gameId));
        return gameId;
    }

    private List<PieceEntity> toPieceEntities(GameState gameState, Long gameId) {
        return gameState.getPointPieces()
            .entrySet()
            .stream()
            .filter(entry -> !isEmpty(entry.getValue()))
            .map(entry -> PieceEntity.from(entry, gameId))
            .collect(Collectors.toList());
    }

    private boolean isEmpty(Piece piece) {
        return piece.isSameType(PieceType.EMPTY);
    }

    public Room findRoomById(Long roomId) {
        RoomEntity entity = findRoomOrThrow(roomId);
        return new Room(entity.getRoomName(), entity.getPassword());
    }

    private RoomEntity findRoomOrThrow(Long roomId) {
        return roomDao.findRoomById(roomId)
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("%d 방 번호에 해당하는 방이 없습니다.", roomId)
            ));
    }

    public void deleteRoom(Long roomId) {
        roomDao.deleteRoom(roomId);
    }

    public List<Room> findAllRooms() {
        List<RoomEntity> entities = roomDao.findAll();
        return entities.stream()
            .map(entity -> new Room(
                entity.getId(),
                entity.getRoomName(),
                entity.getPassword()
            )).collect(Collectors.toList());
    }
}
