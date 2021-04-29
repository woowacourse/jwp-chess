package chess.repository;

import chess.dao.PieceDao;
import chess.dao.RoomDao;
import chess.dao.dto.response.ChessResponseDto;
import chess.dao.dto.response.RoomResponseDto;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
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

    public Map<Long, String> showAllRooms() {
        Map<Long, String> rooms = new LinkedHashMap<>();
        List<RoomResponseDto> roomResponsesDto = roomDao.showAllRooms();
        for (RoomResponseDto roomResponseDto : roomResponsesDto) {
            rooms.put(roomResponseDto.getRoomId(), roomResponseDto.getRoomName());
        }
        return rooms;
    }

    public Map<String, String> showAllPieces(final Long roomId) {
        Map<String, String> pieces = new LinkedHashMap<>();
        List<ChessResponseDto> ChessResponsesDto = pieceDao.showAllPieces(roomId);
        for (ChessResponseDto chessResponseDto : ChessResponsesDto) {
            pieces.put(chessResponseDto.getPiecePosition(), chessResponseDto.getPieceName());
        }
        return pieces;
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
