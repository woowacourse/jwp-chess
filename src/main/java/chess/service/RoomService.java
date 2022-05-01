package chess.service;

import chess.dao.RoomDao;
import chess.dto.request.RoomRequest;
import chess.dto.request.UserPasswordRequest;
import chess.dto.response.RoomResponse;
import chess.exception.ClientException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomDao roomDao;

    public RoomService(final RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public List<RoomResponse> searchRooms() {
        return roomDao.findAll();
    }

    public List<RoomResponse> createRoom(final RoomRequest roomRequest) {
        roomDao.insert(roomRequest.getName(), roomRequest.getPassword());
        return roomDao.findAll();
    }

    public List<RoomResponse> deleteRoomFrom(final String id, final UserPasswordRequest userPasswordRequest) {
        final String roomPassword = roomDao.findPasswordById(id);
        final String userInputPassword = userPasswordRequest.getPassword();
        if (roomPassword.equals(userInputPassword)) {
            roomDao.deleteFrom(id);
            return roomDao.findAll();
        }
        throw new ClientException("입력한 비밀번호가 일치하지 않습니다! \n다시 입력해주세요.");
    }
}
