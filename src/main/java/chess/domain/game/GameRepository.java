package chess.domain.game;

import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.dao.dto.GameDto;
import chess.dao.dto.PieceDto;
import chess.exception.DataNotFoundException;
import java.util.List;
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
        final GameDto gameDto = gameDao.findById(gameId)
            .orElseThrow(() -> new DataNotFoundException(GameDto.class));
        final List<PieceDto> pieceDtos = pieceDao.selectAll(gameId);
        return GameFactory.of(gameDto, pieceDtos);
    }

}
