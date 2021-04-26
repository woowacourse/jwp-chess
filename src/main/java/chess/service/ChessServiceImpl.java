package chess.service;

import chess.controller.web.dto.MoveRequestDto;
import chess.dao.ChessDao;
import chess.dao.dto.ChessGame;
import chess.domain.manager.ChessGameManager;
import chess.domain.manager.ChessGameManagerBundle;
import chess.domain.manager.ChessGameManagerFactory;
import chess.domain.manager.NotStartedChessGameManager;
import chess.domain.piece.attribute.Color;
import chess.domain.position.Position;
import chess.domain.statistics.ChessGameStatistics;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Service
public class ChessServiceImpl implements ChessService {
    private static final long TEMPORARY_ID = 0;

    private final ChessDao chessDao;

    public ChessServiceImpl(ChessDao chessDao) {
        this.chessDao = chessDao;
    }

    @Override
    public ChessGameManager start(String title) {
        ChessGameManager chessGameManager = ChessGameManagerFactory.createRunningGame(TEMPORARY_ID, title);
        long gameId = chessDao.save(new ChessGame(chessGameManager));
        return ChessGameManagerFactory.createRunningGame(gameId, title);
    }

    @Override
    public ChessGameManager end(long gameId) {
        ChessGameManager endGameManager = findById(gameId).end();
        ChessGameManager chessGame = findById(gameId);
        chessGame.end();
        update(endGameManager);
        return endGameManager;
    }

    @Override
    public ChessGameManagerBundle findRunningGames() {
        return chessDao.findAllOnRunning()
                .stream()
                .map(this::createFromEntity)
                .collect(collectingAndThen(toList(), ChessGameManagerBundle::new));
    }

    private ChessGameManager createFromEntity(ChessGame chessGame) {
        return ChessGameManagerFactory.loadingGame(chessGame);
    }

    private void update(ChessGameManager chessGameManager) {
        chessDao.update(new ChessGame(chessGameManager));
    }

    @Override
    public ChessGameManager load(long gameId) {
        return chessDao.findById(gameId)
                .map(ChessGameManagerFactory::loadingGame)
                .orElseGet(() -> new NotStartedChessGameManager(gameId,""));
    }

    @Override
    public void move(long gameId, Position from, Position to) {
        ChessGameManager chessGameManager = findById(gameId);
        chessGameManager.move(from, to);
        if (chessGameManager.isKingDead()) {
            chessGameManager = chessGameManager.end();
        }
        update(chessGameManager);
    }

    @Override
    public void move(long gameId, MoveRequestDto moveRequestDto) {
        move(gameId, moveRequestDto.getFromPosition(), moveRequestDto.getToPosition());
    }

    @Override
    public boolean isEnd(long gameId) {
        return findById(gameId).isEnd();
    }

    @Override
    public ChessGameManager findById(long gameId) {
        return chessDao.findById(gameId)
                .map(ChessGameManagerFactory::loadingGame)
                .orElseGet(() -> new NotStartedChessGameManager(gameId, ""));
    }

    @Override
    public Color nextColor(long gameId) {
        return findById(gameId).nextColor();
    }

    @Override
    public ChessGameStatistics getStatistics(long gameId) {
        return findById(gameId).getStatistics();
    }

    @Override
    public ChessGameManager reset(long gameId) {
        String title = findById(gameId).getTitle();
        ChessGameManager chessGameManager = ChessGameManagerFactory.createRunningGame(gameId, title);
        update(chessGameManager);
        return chessGameManager;
    }

    @Override
    public void delete(long id) {
        chessDao.delete(id);
    }
}
