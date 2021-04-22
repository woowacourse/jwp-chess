package chess.repository;

//import chess.dao.PieceDao;

import chess.dao.PieceDao;
import chess.dao.RoomDao;
import chess.domain.board.ChessBoardFactory;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.request.MoveRequestDto;
import chess.dto.request.PieceDto;
import chess.dto.request.TurnChangeRequestDto;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ChessRepository {
    private final RoomDao roomDao;
    private final PieceDao pieceDao;

    public ChessRepository(RoomDao roomDao, PieceDao pieceDao) {
        this.roomDao = roomDao;
        this.pieceDao = pieceDao;
    }

    public int makeRoom(Map<String, String> board) {
        int newRoomId = roomDao.selectLastRoomId() + 1;
        roomDao.insertRoom(newRoomId);
        initializePieceStatus(newRoomId, board);
        return newRoomId;
    }

    public void initializePieceStatus(int roomId, final Map<String, String> board) {
        for (Map.Entry<String, String> boardStatus : board.entrySet()) {
            pieceDao.insertInitialPieces(roomId, boardStatus.getValue(), boardStatus.getKey());
        }
    }

    public Map<Position, Piece> getBoardByRoomId(int roomId) {
        Map<String, String> board = new LinkedHashMap<>();
        List<PieceDto> pieces = pieceDao.getPieces(roomId);

        for (PieceDto piece : pieces) {
            board.put(piece.getPosition(), piece.getName());
        }
        return ChessBoardFactory.createStoredBoard(board);
    }

    public void movePiece(String source, String target, int roomId) {
        pieceDao.movePiece(source, target, roomId);
    }

    public void changeTurn(String nextTurn, String currentTurn, int roomId) {
        roomDao.changeTurn(nextTurn, currentTurn, roomId);
    }

    public String getCurrentTurnByRoomId(int roomId) {
        return roomDao.selectTurnByRoomId(roomId);
    }

    public void removePiece(String target, int roomId) {
        pieceDao.removePiece(target, roomId);
    }
}
