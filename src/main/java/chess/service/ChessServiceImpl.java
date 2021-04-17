package chess.service;

import chess.controller.web.dto.MoveRequestDto;
import chess.dao.ChessDao;
import chess.chessgame.domain.manager.ChessGameManager;
import chess.chessgame.domain.manager.ChessGameManagerBundle;
import chess.chessgame.domain.manager.ChessGameManagerFactory;
import chess.chessgame.domain.piece.attribute.Color;
import chess.chessgame.domain.position.Position;
import chess.chessgame.repository.ChessGameRepository;
import chess.chessgame.domain.statistics.ChessGameStatistics;
import org.springframework.stereotype.Service;

@Service
public class ChessServiceImpl implements ChessService {
    private static final long TEMPORARY_ID = 0;

    private final ChessGameRepository chessGameRepository;

    public ChessServiceImpl(ChessDao chessDao) {
        this.chessGameRepository = new ChessGameRepository(chessDao);
    }

    @Override
    public ChessGameManager start() {
        long gameId = chessGameRepository.add(ChessGameManagerFactory.createRunningGame(TEMPORARY_ID));
        return ChessGameManagerFactory.createRunningGame(gameId);
    }

    @Override
    public ChessGameManager end(long gameId) {
        ChessGameManager endGameManager = findById(gameId).end();
        update(endGameManager);
        return endGameManager;
    }

    @Override
    public ChessGameManagerBundle findRunningGames() {
        return chessGameRepository.findRunningGames();
    }

    @Override
    public boolean isKindDead(long gameId) {
        return findById(gameId).isKingDead();
    }

    private void update(ChessGameManager chessGameManager) {
        chessGameRepository.update(chessGameManager);
    }

    @Override
    public ChessGameManager load(long gameId) {
        return chessGameRepository.findById(gameId);
    }

    @Override
    public void move(long gameId, Position from, Position to) {
        ChessGameManager chessGameManager = chessGameRepository.findById(gameId);
        chessGameManager.move(from, to);
        if (chessGameManager.isKingDead()) {
            chessGameManager = chessGameManager.end();
        }
        chessGameRepository.update(chessGameManager);
    }

    @Override
    public void move(MoveRequestDto moveRequestDto) {
        move(moveRequestDto.getGameId(), moveRequestDto.getFromPosition(), moveRequestDto.getToPosition());
    }

    @Override
    public boolean isEnd(long gameId) {
        return chessGameRepository.isEnd(gameId);
    }

    @Override
    public ChessGameManager findById(long gameId) {
        return chessGameRepository.findById(gameId);
    }

    @Override
    public Color nextColor(long gameId) {
        return findById(gameId).nextColor();
    }

    @Override
    public ChessGameStatistics getStatistics(long gameId) {
        return findById(gameId).getStatistics();
    }
}
