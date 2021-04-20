package chess.repository;

import chess.dao.PieceDao;
import chess.dao.TurnDao;
import chess.dto.response.ChessResponseDto;
import chess.dto.request.MoveRequestDto;
import chess.dto.request.TurnChangeRequestDto;
import chess.dto.response.TurnResponseDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ChessRepository {
    private final PieceDao pieceDao;
    private final TurnDao turnDao;

    public ChessRepository(PieceDao pieceDao, TurnDao turnDao) {
        this.pieceDao = pieceDao;
        this.turnDao = turnDao;
    }

    public void initializePieceStatus(final Map<String, String> board) {
        for (Map.Entry<String, String> boardStatus : board.entrySet()) {
            pieceDao.initializePieceStatus(boardStatus.getValue(), boardStatus.getKey());
        }
    }

    public void initializeTurn() {
        turnDao.initializeTurn();
    }

    public List<ChessResponseDto> showAllPieces() {
        return pieceDao.showAllPieces();
    }

    public List<TurnResponseDto> showCurrentTurn() {
        return turnDao.showCurrentTurn();
    }

    public void movePiece(final MoveRequestDto moveRequestDto) {
        pieceDao.movePiece(moveRequestDto);
    }

    public void changeTurn(final TurnChangeRequestDto turnChangeRequestDto) {
        turnDao.changeTurn(turnChangeRequestDto);
    }

    public void removeAllPieces() {
        pieceDao.removeAllPieces();
    }

    public void removeTurn() {
        turnDao.removeTurn();
    }

    public void removePiece(final MoveRequestDto moveRequestDto) {
        pieceDao.removePiece(moveRequestDto);
    }
}
