package chess.service;

import chess.dto.RoomInfoDto;
import chess.service.dao.RoomDao;
import org.springframework.stereotype.Service;

import java.util.List;

// TODO :: Dao랑 service가 같이 있는게 걸린다. 분리할 생각

@Service
public class RoomService {
    private final GameService gameService;
    private final RoomDao roomDao;

    public RoomService(final RoomDao roomDao, final GameService gameService) {
        this.roomDao = roomDao;
        this.gameService = gameService;
    }

    public long save(final String roomName, final String player1) {
        final long roomId = roomDao.save(roomName, player1);
        gameService.create(roomId);
        return roomId;
    }

    public void delete(final long roomId) {
        gameService.delete(roomId);
        roomDao.delete(roomId);
    }

    public List<RoomInfoDto> loadList() {
        return roomDao.loadRooms();
    }

    public RoomInfoDto roomInfo(final long roomId) {
        return new RoomInfoDto(roomId, roomDao.name(roomId));
    }
}
