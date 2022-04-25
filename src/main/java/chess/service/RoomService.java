package chess.service;

import chess.dao.RoomDao;

public class RoomService {

    private final RoomDao roomDao;

    public RoomService(final RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public RoomServiceMessage saveNewRoom(final String roomName, final String passWord) {
        if (roomDao.isDuplicatedName(roomName)) {
            return RoomServiceMessage.ROOM_CREATE_FAIL_BY_DUPLICATED_NAME;
        }
        roomDao.saveNewRoom(roomName, passWord);
        return RoomServiceMessage.ROOM_CREATE_SUCCESS;
    }
}
