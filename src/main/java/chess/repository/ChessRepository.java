package chess.repository;

import chess.dao.PieceDao;
import chess.dao.RoomDao;
import chess.dto.response.ChessResponseDto;
import chess.dto.response.RoomResponseDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ChessRepository {
    private final RoomDao roomDao;
    private final PieceDao pieceDao;

    public ChessRepository(final RoomDao roomDao, final PieceDao pieceDao) {
        this.roomDao = roomDao;
        this.pieceDao = pieceDao;
    }

    public Long addRoom(final String roomName) {
        return roomDao.addRoom(roomName);
    }

    public void initializePieceStatus(final Map<String, String> board, final Long roomId) {
        for (Map.Entry<String, String> boardStatus : board.entrySet()) {
            pieceDao.initializePieceStatus(boardStatus.getValue(), boardStatus.getKey(), roomId);
        }
    }

    public List<RoomResponseDto> showAllRooms() {
        return roomDao.showAllRooms();
    }

    public List<ChessResponseDto> showAllPieces(final Long roomId) {
        return pieceDao.showAllPieces(roomId);
    }

    public String showCurrentTurn(final Long roomId) {
        return roomDao.showCurrentTurn(roomId);
    }

    public void movePiece(final String source, final String target) {
        pieceDao.movePiece(source, target);
    }

    public void changeTurn(final String nextTurn, final Long roomId) {
        roomDao.changeTurn(nextTurn, roomId);
    }

    public void removePiece(final String target) {
        pieceDao.removePiece(target);
    }
}
