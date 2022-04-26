package chess.service;

import chess.dao.ChessGameDao;
import chess.domain.ChessGameRoom;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ChessGameRoomService {

    private final ChessGameDao chessGameDao;

    public ChessGameRoomService(final ChessGameDao chessGameDao) {
        this.chessGameDao = chessGameDao;
    }

    public List<ChessGameRoom> findAllChessGameRooms() {
        return chessGameDao.findAllChessGames();
    }
}
