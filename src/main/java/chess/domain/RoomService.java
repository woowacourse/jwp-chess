package chess.domain;

import chess.dao.RoomDao;
import chess.dto.RoomResponseDto;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RoomService {

    private static final int SUCCEED_COUNT = 0;

    private final RoomDao roomDao;

    public RoomService(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public int createRoom(String name, String pw) {
        return roomDao.create(name, pw);
    }

    public void deleteRoom(String pw, int roomId) {
        int returnDeleteColumnCount = roomDao.deleteRoom(pw, roomId);
        if (returnDeleteColumnCount == SUCCEED_COUNT) {
            throw new IllegalArgumentException("비밀번호가 달라 방을 지울 수 없습니다.");
        }
    }

    public List<RoomResponseDto> getRooms() {
        return roomDao.getRooms();
    }
}
