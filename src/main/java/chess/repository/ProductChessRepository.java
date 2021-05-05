package chess.repository;

import chess.dao.PieceDao;
import chess.dao.RoomDao;
import chess.domain.board.ChessBoardFactory;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.PieceDto;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductChessRepository implements ChessRepository {
    private final RoomDao roomDao;
    private final PieceDao pieceDao;

    public ProductChessRepository(RoomDao roomDao, PieceDao pieceDao) {
        this.roomDao = roomDao;
        this.pieceDao = pieceDao;
    }

    @Override
    public int makeRoom(Map<String, String> board, String roomName) {
        int newRoomId = roomDao.insertRoom(roomName);
        initializePieceStatus(newRoomId, board);
        return newRoomId;
    }

    @Override
    public void initializePieceStatus(int roomId, final Map<String, String> board) {
        for (Map.Entry<String, String> boardStatus : board.entrySet()) {
            pieceDao.insertInitialPieces(roomId, boardStatus.getValue(), boardStatus.getKey());
        }
    }

    @Override
    public Map<Position, Piece> getBoardByRoomId(int roomId) {
        Map<String, String> board = new LinkedHashMap<>();
        List<PieceDto> pieces = pieceDao.getPieces(roomId);

        for (PieceDto piece : pieces) {
            board.put(piece.getPosition(), piece.getName());
        }
        return ChessBoardFactory.createStoredBoard(board);
    }

    @Override
    public void moveSourcePieceToTargetPoint(String source, String target, int roomId) {
        pieceDao.movePiece(source, target, roomId);
    }

    @Override
    public void changeTurn(String nextTurn, String currentTurn, int roomId) {
        roomDao.changeTurn(nextTurn, currentTurn, roomId);
    }

    @Override
    public String getCurrentTurnByRoomId(int roomId) {
        return roomDao.selectTurnByRoomId(roomId);
    }

    @Override
    public void removeTargetPiece(String target, int roomId) {
        pieceDao.removePiece(target, roomId);
    }

    @Override
    public Map<Integer, String> getRoomNames() {
        List<Map<String, Object>> resultList = roomDao.selectAllRoomNames();
        Map<Integer, String> rooms = new HashMap<>();
        for (Map<String, Object> result : resultList) {
            int roomId = (int) result.get("room_id");
            String roomName = (String) result.get("room_name");
            rooms.put(roomId, roomName);
        }
        return rooms;
    }

    @Override
    public int getRoomId(String roomName) {
        return roomDao.selectRoomId(roomName);
    }
}
