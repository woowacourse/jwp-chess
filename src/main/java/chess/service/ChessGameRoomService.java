package chess.service;

import chess.controller.dto.request.ChessGameRoomDeleteRequest;
import chess.dao.ChessGameDao;
import chess.domain.ChessGameRoom;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ChessGameRoomService {

    private final ChessGameDao chessGameDao;

    public ChessGameRoomService(ChessGameDao chessGameDao) {
        this.chessGameDao = chessGameDao;
    }

    public List<ChessGameRoom> findAllChessGameRooms() {
        return chessGameDao.findAllChessGames();
    }

    public void deleteChessRoom(long chessGameId, ChessGameRoomDeleteRequest deleteRequest) {
        ChessGameRoom chessGameRoom = chessGameDao.findChessGameRoom(chessGameId);
        chessGameRoom.checkCanDelete(deleteRequest.getPassword());
        chessGameDao.deleteChessGame(chessGameRoom.getId());
    }
}
