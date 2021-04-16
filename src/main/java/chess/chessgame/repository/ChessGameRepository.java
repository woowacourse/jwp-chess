package chess.chessgame.repository;

import chess.dao.ChessDao;
import chess.dao.dto.ChessGame;
import chess.chessgame.domain.manager.ChessGameManager;
import chess.chessgame.domain.manager.ChessGameManagerBundle;
import chess.chessgame.domain.manager.ChessGameManagerFactory;
import chess.chessgame.domain.manager.NotStartedChessGameManager;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Repository
public class ChessGameRepository {
    private final ChessDao chessDao;

    public ChessGameRepository(ChessDao chessDao) {
        this.chessDao = chessDao;
    }

    public ChessGameManager findById(long id) {
        return chessDao.findById(id).map(ChessGameManagerFactory::loadingGame)
                .orElseGet(() -> new NotStartedChessGameManager(id));
    }

    public ChessGameManagerBundle findRunningGames() {
        return chessDao.findAllOnRunning().stream()
                .map(this::createFromEntity)
                .collect(collectingAndThen(toList(), ChessGameManagerBundle::new));
    }

    private ChessGameManager createFromEntity(ChessGame chessGame) {
        return ChessGameManagerFactory.loadingGame(chessGame);
    }

    public long add(ChessGameManager chessGameManager) {
        return chessDao.save(new ChessGame(chessGameManager));
    }

    public void update(ChessGameManager chessGameManager) {
        chessDao.update(new ChessGame(chessGameManager));
    }

    public void delete(long id) {
        chessDao.delete(id);
    }

    public boolean isEnd(long gameId) {
        return findById(gameId).isEnd();
    }
}
