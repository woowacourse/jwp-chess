package chess.domain.game;

import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.dao.dto.GameDto;
import chess.dao.dto.PieceDto;
import chess.domain.piece.Piece;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepository {

    private final GameDao gameDao;
    private final PieceDao pieceDao;

    public GameRepository(final GameDao gameDao, final PieceDao pieceDao) {
        this.gameDao = gameDao;
        this.pieceDao = pieceDao;
    }

    public Game findById(final long gameId) {
        final GameDto gameDto = gameDao.findById(gameId);
        final List<PieceDto> pieceDtos = pieceDao.selectAll(gameId);
        return GameFactory.of(gameDto, pieceDtos);
    }

    public void deleteRemovedPieces(final long gameId, final Game game) {
        final List<Piece> pieces = findById(gameId).toPieces();
        final List<Piece> modifiedPieces = game.toPieces();
        pieces.removeAll(modifiedPieces);
        deletePieces(pieces);
    }

    private void deletePieces(final List<Piece> pieces) {
        final List<Long> ids = pieces.stream()
            .map(Piece::getId)
            .collect(Collectors.toList());
        pieceDao.deleteBatchByIds(ids);
    }

}
