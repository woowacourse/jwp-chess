package chess.service;

import chess.dao.PieceDao;
import chess.domain.piece.PieceFactory;
import chess.dto.PieceDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PieceService {

    private final PieceDao pieceDao;

    public PieceService(PieceDao pieceDao) {
        this.pieceDao = pieceDao;
    }

    public void createInitialPieces(long gameId) {
        pieceDao.insertAll(gameId, PieceFactory.createPieces());
    }

    public List<PieceDto> findPiecesByGameId(long gameId) {
        return pieceDao.selectAll(gameId)
                .stream()
                .map(PieceDto::from)
                .collect(Collectors.toList());
    }
}
