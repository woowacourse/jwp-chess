package chess.service;

import chess.dao.RoomDao;
import chess.dto.RoomDto;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private static final String PLAYING_STATE_SYMBOL = "playing";

    private final RoomDao roomDao;

    public RoomService(final RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public void saveNewRoom(final String roomName, final String passWord) {
        if (roomDao.hasDuplicatedName(roomName)) {
            throw new IllegalArgumentException("이미 동일한 이름의 체스방이 존재합니다.");
        }
        roomDao.saveNewRoom(roomName, passWord);
    }

    public void deleteRoom(final int roomId, final String password) {
        if (isIncorrectPassword(roomId, password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        if (isPlayingState(roomId)) {
            throw new IllegalStateException("게임이 진행중인 체스방은 삭제할 수 없습니다.");
        }
        roomDao.deleteRoomByName(roomId);
    }

    private boolean isIncorrectPassword(final int roomId, final String password) {
        final String savedPassword = roomDao.getPasswordByName(roomId);
        return !password.equals(savedPassword);
    }

    private boolean isPlayingState(final int roomId) {
        final String savedGameState = roomDao.getGameStateByName(roomId);
        return savedGameState.equals(PLAYING_STATE_SYMBOL);
    }

    public List<RoomDto> getRoomNames() {
        return roomDao.getRoomNames();
    }
}
