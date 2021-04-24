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

    public PieceService(PieceDao pieceDao) {
        this.pieceDao = pieceDao;
    }

    public void createInitialPieces(long gameId) {
        pieceDao.insertAll(gameId, Board.createWithInitialLocation().toList());
    }

    public List<PieceDto> findPiecesByGameId(long gameId) {
        return pieceDao.selectAll(gameId)
            .stream()
            .map(PieceDto::from)
            .collect(Collectors.toList());
    }

    public void move(long gameId, MoveRequestDto moveRequestDto) {
        pieceDao.updatePosition(gameId, Location.convert(moveRequestDto.getSource()),
            Location.convert(moveRequestDto.getTarget()));
    }

    public void catchPiece(long gameId, MoveRequestDto moveRequestDto) {
        pieceDao.delete(gameId, Location.convert(moveRequestDto.getTarget()));
    }

    public void removeAll(long gameId) {
        pieceDao.deletePieces(gameId);
    }
}
