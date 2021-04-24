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
        return roomDao.load();
    }

    public RoomInfoDto roomInfo(final long roomId) {
        return new RoomInfoDto(roomId, roomDao.name(roomId));
    }

    public void enter(final long roomId, final String playerId) {
        if(roomDao.isJoined(roomId, playerId)){
            return;
        }

        if(roomDao.isFull(roomId)){
            throw new IllegalArgumentException("이미 가득 찬 방입니다.");
        }

        roomDao.enter(roomId, playerId);
    }
}
