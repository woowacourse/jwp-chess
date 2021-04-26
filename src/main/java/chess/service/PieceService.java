package chess.service;

import chess.dao.PieceDao;
import chess.domain.board.Board;
import chess.domain.location.Location;
import chess.dto.chess.MoveRequestDto;
import chess.dto.piece.PieceDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class PieceService {

    private final PieceDao pieceDao;

    public PieceService(final PieceDao pieceDao) {
        this.pieceDao = pieceDao;
    }

    public void createInitialPieces(final long gameId) {
        pieceDao.insertAll(gameId, Board.createWithInitialLocation().toList());
    }

    public List<PieceDto> findPiecesByGameId(final long gameId) {
        return pieceDao.selectAll(gameId)
            .stream()
            .map(PieceDto::from)
            .collect(Collectors.toList());
    }

    public void move(final long gameId, final MoveRequestDto moveRequestDto) {
        pieceDao.updatePosition(gameId, Location.convert(moveRequestDto.getSource()),
            Location.convert(moveRequestDto.getTarget()));
    }

    public void catchPiece(final long gameId, final MoveRequestDto moveRequestDto) {
        pieceDao.delete(gameId, Location.convert(moveRequestDto.getTarget()));
    }

    public void removeAll(final long gameId) {
        pieceDao.deletePieces(gameId);
    }

}
