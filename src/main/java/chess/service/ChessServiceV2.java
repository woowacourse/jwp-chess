package chess.service;

import chess.dao.RoomDao;
import org.springframework.stereotype.Service;

@Service
public class ChessServiceV2 {

    private final RoomDao roomDao;

    public ChessServiceV2(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public Long insertRoom(String title, String password) {
        validateValueIsBlank(title, password);
        final Long roomId = roomDao.insertRoom(title, password);

        return roomId;
    }

    private void validateValueIsBlank(String title, String password) {
        if (title.isBlank()) {
            throw new IllegalArgumentException("제목이 빈칸입니다.");
        }
        if (password.isBlank()) {
            throw new IllegalArgumentException("비밀번호가 빈칸입니다");
        }
    }
}
