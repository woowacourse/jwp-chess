package chess.service;

import chess.chessgame.repository.ChessGameManagerRepository;
import chess.controller.web.dto.MoveRequestDto;
import chess.chessgame.domain.manager.ChessGameManager;
import chess.chessgame.domain.manager.ChessGameManagerBundle;
import chess.chessgame.domain.manager.ChessGameManagerFactory;
import chess.chessgame.domain.piece.attribute.Color;
import chess.chessgame.domain.position.Position;
import chess.chessgame.domain.statistics.ChessGameStatistics;
import org.springframework.stereotype.Service;

@Service
public class ChessServiceImpl implements ChessService {
    private static final long TEMPORARY_ID = 0;

    private final ChessGameManagerRepository chessGameManagerRepository;

    public ChessServiceImpl(ChessGameManagerRepository chessGameManagerRepository) {
        this.chessGameManagerRepository = chessGameManagerRepository;
    }

    @Override
    public ChessGameManager start() {
        long gameId = chessGameManagerRepository.add(ChessGameManagerFactory.createRunningGame(TEMPORARY_ID));
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
        return chessGameManagerRepository.findRunningGames();
    }

    @Override
    public boolean isKindDead(long gameId) {
        return findById(gameId).isKingDead();
    }

    private void update(ChessGameManager chessGameManager) {
        chessGameManagerRepository.update(chessGameManager);
    }

    @Override
    public ChessGameManager load(long gameId) {
        return chessGameManagerRepository.findById(gameId);
    }

    @Override
    public void move(long gameId, Position from, Position to) {
        ChessGameManager chessGameManager = chessGameManagerRepository.findById(gameId);
        chessGameManager.move(from, to);
        if (chessGameManager.isKingDead()) {
            chessGameManager = chessGameManager.end();
        }
        chessGameManagerRepository.update(chessGameManager);
    }

    @Override
    public void move(MoveRequestDto moveRequestDto) {
        move(moveRequestDto.getGameId(), Position.of(moveRequestDto.getFrom()), Position.of(moveRequestDto.getTo()));
    }

    @Override
    public boolean isEnd(long gameId) {
        return chessGameManagerRepository.isEnd(gameId);
    }

    @Override
    public ChessGameManager findById(long gameId) {
        return chessGameManagerRepository.findById(gameId);
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
