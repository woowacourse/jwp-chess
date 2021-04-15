package chess.service;

import chess.controller.dto.RoomInfoDto;
import chess.service.dao.RoomDao;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class RoomService {

    private final RoomDao roomDao;

    public RoomService(final RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public Long save(final String roomName) {
        final boolean isDuplicated = roomDao.load().stream()
                .map(roomInfoDto -> roomInfoDto.getName())
                .anyMatch(name -> name.equals(roomName));

        if (isDuplicated) {
            throw new IllegalArgumentException("중복된 방 이름입니다.");
        }

        final Long roomId = System.currentTimeMillis();
        roomDao.save(roomName, roomId);
        return roomId;
    }

    public void delete(final Long roomId) {
        roomDao.delete(roomId);
    }

    public List<RoomInfoDto> loadList() {
        return roomDao.load();
    }

    public RoomInfoDto roomInfo(final Long roomId) throws SQLException {
        return new RoomInfoDto(roomId, roomDao.name(roomId));
    }
}
