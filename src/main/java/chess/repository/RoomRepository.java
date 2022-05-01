package chess.repository;

import chess.dao.RoomDao;
import chess.dto.RoomDto;
import chess.entity.RoomEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class RoomRepository {

    private final RoomDao roomDao;

    public RoomRepository(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public void save(RoomDto roomDto) {
        RoomEntity roomEntity = RoomEntity.of(
                roomDto.getId(),
                roomDto.getName(),
                roomDto.getPassword(),
                roomDto.getStatus());
        roomDao.insert(roomEntity);
    }

    public List<RoomDto> findAll() {
        return convertToRoomDto(roomDao.findAll());
    }

    public void deleteRoom(int gameId) {
        roomDao.delete(RoomEntity.of(gameId));
    }

    public String findPasswordById(int gameId) {
        return roomDao.findById(RoomEntity.of(gameId)).getRoomPassword();
    }

    public void updateStatus(int gameId, String status) {
        RoomEntity roomEntity = roomDao.findById(RoomEntity.of(gameId));
        roomDao.updateById(RoomEntity.of(gameId,
                roomEntity.getRoomName(),
                roomEntity.getRoomPassword(),
                status));
    }

    private List<RoomDto> convertToRoomDto(List<RoomEntity> roomEntities) {
        return roomEntities.stream()
                .map(it -> new RoomDto(it.getGameId(), it.getRoomName(), it.getRoomPassword(), it.getStatus()))
                .collect(Collectors.toList());
    }
}
