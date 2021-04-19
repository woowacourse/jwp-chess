package chess.service;

import chess.controller.dto.RoomInfoDto;
import chess.service.dao.RoomDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final GameService gameService;
    private final RoomDao roomDao;

    public RoomService(final RoomDao roomDao, final GameService gameService) {
        this.roomDao = roomDao;
        this.gameService = gameService;
    }

    public Long save(final String roomName) {
        final Long roomId = System.currentTimeMillis();
        roomDao.save(roomName, roomId);
        gameService.create(roomId);
        return roomId;
    }

    public void delete(final Long roomId) {
        gameService.delete(roomId);
        roomDao.delete(roomId);
    }

    public List<RoomInfoDto> loadList() {
        return roomDao.load();
    }

    public RoomInfoDto roomInfo(final Long roomId) {
        return new RoomInfoDto(roomId, roomDao.name(roomId));
    }
}
