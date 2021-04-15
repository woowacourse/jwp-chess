package chess.service;

import chess.dao.PieceDao;
import chess.domain.piece.PieceFactory;
import org.springframework.stereotype.Service;

@Service
public class PieceService {

    private final PieceDao pieceDao;

    public PieceService(PieceDao pieceDao) {
        this.pieceDao = pieceDao;
    }

    public void createInitialPieces(long gameId) {
        pieceDao.insertAll(gameId, PieceFactory.createPieces());
    }
}
