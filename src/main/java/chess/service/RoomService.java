package chess.service;

import chess.dao.RoomDao;
import chess.dto.CommonDto;
import chess.dto.RoomDto;
import chess.dto.RoomListDto;
import chess.exception.HandledException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class RoomService {
    private final RoomDao roomDao;

    public RoomService(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public CommonDto<RoomListDto> list() {
        return new CommonDto<>("게임 목록을 불러왔습니다.", new RoomListDto(roomDao.loadRoomList()));
    }

    public CommonDto<RoomDto> save(RoomDto roomDto) {
        String roomName = roomDto.getName();
        int gameId = roomDto.getGameId();

        if (roomDao.countRoomByName(roomName) != 0) {
            throw new HandledException("방이 이미 등록되어있습니다.");
        }

        roomDao.saveRoom(gameId, roomName);
        return new CommonDto<>("방 정보를 가져왔습니다.", new RoomDto(gameId, roomName));
    }

    public String loadRoomName(int gameId) {
        return roomDao.loadRoomName(gameId);
    }
}
