package chess.service;

import chess.dao.RoomDao;

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

    public void deleteRoom(final String roomName, final String password) {
        if (isIncorrectPassword(roomName, password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        if (isPlayingState(roomName)) {
            throw new IllegalStateException("게임이 진행중인 체스방은 삭제할 수 없습니다.");
        }
        roomDao.deleteRoomByName(roomName);
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
