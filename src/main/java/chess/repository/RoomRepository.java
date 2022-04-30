package chess.repository;

import chess.dao.ChessPieceDao;
import chess.dao.RoomDao;
import chess.domain.ChessGame;
import chess.domain.chessboard.ChessBoard;
import chess.domain.chesspiece.ChessPiece;
import chess.domain.position.Position;
import chess.domain.room.Room;
import chess.dto.response.RoomResponseDto;
import chess.entity.ChessPieceEntity;
import chess.entity.RoomEntity;
import chess.exception.NotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class RoomRepository {

    private final RoomDao roomDao;
    private final ChessPieceDao chessPieceDao;

    public RoomRepository(final RoomDao roomDao, final ChessPieceDao chessPieceDao) {
        this.roomDao = roomDao;
        this.chessPieceDao = chessPieceDao;
    }

    public Room get(final int roomId) {
        try {
            final RoomEntity roomEntity = roomDao.findById(roomId);
            return toRoom(roomEntity);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("방이 존재하지 않습니다.");
        }
    }

    public List<RoomResponseDto> getAll() {
        return roomDao.findAllEntity()
                .stream()
                .map(roomEntity -> RoomResponseDto.of(
                        roomEntity.getRoomId(),
                        roomEntity.getName(),
                        roomEntity.getGameStatus()
                ))
                .collect(Collectors.toList());
    }

    private Room toRoom(final RoomEntity roomEntity) {
        final List<ChessPieceEntity> chessPieceEntityList = chessPieceDao.findAllEntityByRoomId(roomEntity.getRoomId());

        final Map<Position, ChessPiece> pieceByPosition = chessPieceEntityList.stream()
                .collect(Collectors.toMap(
                        ChessPieceEntity::toPosition,
                        ChessPieceEntity::toChessPiece
                ));
        final ChessBoard chessBoard = new ChessBoard(pieceByPosition, roomEntity.toCurrentTurn());
        final ChessGame chessGame = new ChessGame(chessBoard, roomEntity.toGameStatus());

        return new Room(roomEntity.toRoomName(), roomEntity.toPassword(), chessGame);
    }

    public int add(final Room room) {
        final boolean existName = roomDao.isExistName(room.getName());
        if (existName) {
            throw new IllegalArgumentException("이름이 같은 방이 이미 존재합니다.");
        }
        return roomDao.save(room.getName(), room.getGameStatus(), room.getCurrentTurn(), room.getPassword());
    }

    public void update(final int roomId, final Room room) {
        roomDao.updateById(roomId, room.getGameStatus(), room.getCurrentTurn());
    }
}
