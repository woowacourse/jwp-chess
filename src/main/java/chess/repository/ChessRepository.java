package chess.repository;

import chess.dao.PieceDao;
import chess.dao.RoomDao;
import chess.dto.request.MoveRequestDto;
import chess.dto.request.RoomNameRequestDto;
import chess.dto.request.TurnChangeRequestDto;
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

    public Long addRoom(final RoomNameRequestDto roomNameRequestDto) {
        return roomDao.addRoom(roomNameRequestDto);
    }

    public void initializePieceStatus(final Map<String, String> board, Long roomId) {
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

    public void movePiece(final MoveRequestDto moveRequestDto) {
        pieceDao.movePiece(moveRequestDto);
    }

    public void changeTurn(final TurnChangeRequestDto turnChangeRequestDto) {
        roomDao.changeTurn(turnChangeRequestDto);
    }

    public void removePiece(final MoveRequestDto moveRequestDto) {
        pieceDao.removePiece(moveRequestDto);
    }
}
