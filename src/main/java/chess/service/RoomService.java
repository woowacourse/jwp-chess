package chess.service;

import chess.dao.RoomDao;
import chess.domain.GameStatus;
import chess.domain.chesspiece.Color;
import chess.dto.CurrentTurnDto;
import chess.dto.RoomNameDto;
import chess.dto.RoomStatusDto;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomDao roomDao;

    public RoomService(final RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public List<RoomNameDto> findAllRoomName() {
        return roomDao.findAllRoomName();
    }

    public void createRoom(final RoomNameDto roomNameDto) {
//        roomDao.save(roomNameDto.getName(), GameStatus.READY, Color.WHITE);
    }

    public void deleteRoom(final String roomName) {
        checkRoomExist(roomName);
        roomDao.delete(roomName);
    }

    public CurrentTurnDto findCurrentTurn(final String roomName) {
        checkRoomExist(roomName);
        return roomDao.findCurrentTurnByName(roomName);
    }

    private void checkRoomExist(final String roomName) {
        if (!roomDao.isExistName(roomName)) {
            throw new IllegalArgumentException("존재하지 않는 방 입니다.");
        }
    }
}
