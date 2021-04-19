package chess.service;

import chess.controller.dto.RoomInfoDto;
import chess.service.dao.RoomDao;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
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
        if(isRoomExist(roomName)){
            throw new IllegalArgumentException("중복된 방 이름입니다.");
        }

        final Long roomId = System.currentTimeMillis();
        roomDao.save(roomName, roomId);
        gameService.create(roomId);
        return roomId;
    }

    private boolean isRoomExist(final String roomName){
        return roomDao.load().stream()
                .map(roomInfoDto -> roomInfoDto.getName())
                .anyMatch(name -> name.equals(roomName));
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
