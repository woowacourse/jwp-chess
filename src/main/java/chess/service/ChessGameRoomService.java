package chess.service;

import chess.controller.dto.request.ChessGameRoomDeleteRequest;
import chess.dao.ChessGameDao;
import chess.dao.PieceDao;
import chess.domain.ChessGameRoom;
import chess.domain.piece.PieceFactory;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChessGameRoomService {

    private final PieceDao pieceDao;
    private final ChessGameDao chessGameDao;

    public ChessGameRoomService(PieceDao pieceDao, ChessGameDao chessGameDao) {
        this.pieceDao = pieceDao;
        this.chessGameDao = chessGameDao;
    }

    public List<ChessGameRoom> findAllChessGameRooms() {
        return chessGameDao.findAllChessGames();
    }

    public long createNewChessGame(ChessGameRoom chessGameRoom) {
        long chessGameId = chessGameDao.createChessGame(chessGameRoom);
        pieceDao.savePieces(chessGameId, PieceFactory.createNewChessBoard());
        return chessGameId;
    }

    public void deleteChessRoom(long chessGameId, ChessGameRoomDeleteRequest deleteRequest) {
        ChessGameRoom chessGameRoom = chessGameDao.findChessGameRoom(chessGameId);
        chessGameRoom.checkCanDelete(deleteRequest.getPassword());
        chessGameDao.deleteChessGame(chessGameRoom.getId());
    }
}
