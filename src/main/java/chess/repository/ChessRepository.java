package chess.repository;

import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.dao.RoomDao;
import chess.dao.TeamDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

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
}
