package chess.service;

import chess.domain.ChessGame;
import chess.dto.RoomDto;
import chess.service.dao.GameDao;
import chess.service.dao.RoomDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    private final RoomDao roomDao;
    private final GameDao gameDao;

    public RoomService(final RoomDao roomDao, final GameDao gameDao) {
        this.roomDao = roomDao;
        this.gameDao = gameDao;
    }

    public long create(final String roomName, final String player1) {
        final ChessGame chessGame = ChessGame.initNew();
        final Long roomId = roomDao.save(roomName, player1);
        gameDao.save(roomId, chessGame.turn(), chessGame.board());
        return roomId;
    }

    public void delete(final Long roomId) {
        gameDao.delete(roomId);
        roomDao.delete(roomId);
    }

    public List<RoomDto> loadList() {
        return roomDao.loadRooms();
    }

    public RoomDto roomInfo(final Long roomId) {
        return new RoomDto(roomId, roomDao.name(roomId));
    }
}
