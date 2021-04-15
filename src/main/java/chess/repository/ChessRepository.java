package chess.repository;

import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.dao.RoomDao;
import chess.dao.TeamDao;
import chess.domain.ChessGame;
import chess.domain.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChessRepository {
    @Autowired
    RoomDao roomDao;
    @Autowired
    GameDao gameDao;
    @Autowired
    TeamDao teamDao;
    @Autowired
    PieceDao pieceDao;

    public void createRoom(ChessGame chessGame, Room room) {
        Long gameId = gameDao.create(chessGame);
        room.setGameId(gameId);
        roomDao.create(room);

    }

    public List<Room> loadAllRoom() {
        return roomDao.loadAllRoom();
    }
}
