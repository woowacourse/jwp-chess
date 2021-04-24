package chess.mysql;

import chess.chessgame.domain.room.game.ChessGameManager;
import chess.chessgame.domain.room.game.ChessGameManagerBundle;
import chess.chessgame.domain.room.game.ChessGameManagerFactory;
import chess.chessgame.domain.room.game.NotStartedChessGameManager;
import chess.chessgame.repository.ChessGameManagerRepository;
import chess.mysql.dao.ChessDao;
import chess.mysql.dao.dto.ChessGameDto;
import org.springframework.stereotype.Repository;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Repository
public class ChessGameManagerRepositoryImpl implements ChessGameManagerRepository {
    private final ChessDao chessDao;

    public ChessGameManagerRepositoryImpl(ChessDao chessDao) {
        this.chessDao = chessDao;
    }

    @Override
    public ChessGameManager findById(long id) {
        return chessDao.findById(id).map(ChessGameManagerFactory::loadingGame)
                .orElseGet(() -> new NotStartedChessGameManager(id));
    }

    @Override
    public ChessGameManagerBundle findRunningGames() {
        return chessDao.findAllOnRunning().stream()
                .map(this::createFromEntity)
                .collect(collectingAndThen(toList(), ChessGameManagerBundle::new));
    }

    private ChessGameManager createFromEntity(ChessGameDto chessGameDto) {
        return ChessGameManagerFactory.loadingGame(chessGameDto);
    }

    @Override
    public long add(ChessGameManager chessGameManager) {
        return chessDao.save(new ChessGameDto(chessGameManager));
    }

    @Override
    public void update(ChessGameManager chessGameManager) {
        chessDao.update(new ChessGameDto(chessGameManager));
    }

    @Override
    public void delete(long id) {
        chessDao.delete(id);
    }

    @Override
    public boolean isEnd(long gameId) {
        return findById(gameId).isEnd();
    }

}
