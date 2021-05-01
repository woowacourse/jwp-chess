package chess.mysql.chess;

import chess.chessgame.domain.room.game.ChessGameManager;
import chess.chessgame.domain.room.game.ChessGameManagerBundle;
import chess.chessgame.domain.room.game.ChessGameManagerFactory;
import chess.chessgame.domain.room.game.NotStartedChessGameManager;
import chess.chessgame.repository.ChessGameManagerRepository;
import org.springframework.stereotype.Repository;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Repository
public class ChessGameManagerRepositoryImpl implements ChessGameManagerRepository {
    private static final int TEMPORARY_ID = 0;

    private final ChessDaoImpl chessDao;

    public ChessGameManagerRepositoryImpl(ChessDaoImpl chessDao) {
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

    @Override
    public ChessGameManager create() {
        return add(ChessGameManagerFactory.createRunningGame(TEMPORARY_ID));
    }

    private ChessGameManager createFromEntity(ChessGameDto chessGameDto) {
        return ChessGameManagerFactory.loadingGame(chessGameDto);
    }

    @Override
    public ChessGameManager add(ChessGameManager chessGameManager) {
        return ChessGameManagerFactory.loadingGame(chessDao.save(new ChessGameDto(chessGameManager)));
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
