package chess.repository;

import chess.dao.PieceDao;
import chess.dao.TurnDao;
import chess.dto.request.ChessRequestDto;
import chess.dto.request.MoveRequestDto;
import chess.dto.request.TurnChangeRequestDto;
import chess.dto.request.TurnRequestDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ChessRepositoryImpl implements ChessRepository {
    private final PieceDao pieceDao;
    private final TurnDao turnDao;

    public ChessRepositoryImpl(PieceDao pieceDao, TurnDao turnDao) {
        this.pieceDao = pieceDao;
        this.turnDao = turnDao;
    }

    @Override
    public void initializePieceStatus(final Map<String, String> board) {
        for (Map.Entry<String, String> boardStatus : board.entrySet()) {
            pieceDao.initializePieceStatus(boardStatus.getValue(), boardStatus.getKey());
        }
    }

    @Override
    public void initializeTurn() {
        turnDao.initializeTurn();
    }

    @Override
    public List<ChessRequestDto> showAllPieces() {
        return pieceDao.showAllPieces();
    }

    @Override
    public List<TurnRequestDto> showCurrentTurn() {
        return turnDao.showCurrentTurn();
    }

    @Override
    public void movePiece(final MoveRequestDto moveRequestDto) {
        pieceDao.movePiece(moveRequestDto);
    }

    @Override
    public void changeTurn(final TurnChangeRequestDto turnChangeRequestDto) {
        turnDao.changeTurn(turnChangeRequestDto);
    }

    @Override
    public void removeAllPieces() {
        pieceDao.removeAllPieces();
    }

    @Override
    public void removeTurn() {
        turnDao.removeTurn();
    }

    @Override
    public void removePiece(final MoveRequestDto moveRequestDto) {
        pieceDao.removePiece(moveRequestDto);
    }
}
