package chess.service;

import chess.dao.RoomDao;

public class RoomService {

    private static final String PLAYING_STATE_SYMBOL = "playing";

    private final RoomDao roomDao;

    public RoomService(final RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public RoomServiceMessage saveNewRoom(final String roomName, final String passWord) {
        if (roomDao.hasDuplicatedName(roomName)) {
            return RoomServiceMessage.ROOM_CREATE_FAIL_BY_DUPLICATED_NAME;
        }
        roomDao.saveNewRoom(roomName, passWord);
        return RoomServiceMessage.ROOM_CREATE_SUCCESS;
    }

    public RoomServiceMessage deleteRoom(final String roomName, final String password) {
        if (isIncorrectPassword(roomName, password)) {
            return RoomServiceMessage.ROOM_DELETE_FAIL_BY_WRONG_PASSWORD;
        }
        if (isPlayingState(roomName)) {
            return RoomServiceMessage.ROOM_DELETE_FAIL_BY_NOT_END_GAME;
        }
        return RoomServiceMessage.ROOM_DELETE_SUCCESS;
    }

    private boolean isIncorrectPassword(final String roomName, final String password) {
        final String savedPassword = roomDao.getPasswordByName(roomName);
        return !password.equals(savedPassword);
    }

    private boolean isPlayingState(final String roomName) {
        final String savedGameState = roomDao.getGameStateByName(roomName);
        return savedGameState.equals(PLAYING_STATE_SYMBOL);
    }
}
