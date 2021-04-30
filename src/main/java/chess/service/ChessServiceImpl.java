package chess.service;

import chess.chessgame.domain.room.game.ChessGameManager;
import chess.chessgame.domain.room.game.ChessGameManagerBundle;
import chess.chessgame.domain.room.game.board.piece.attribute.Color;
import chess.chessgame.domain.room.game.board.position.Position;
import chess.chessgame.domain.room.game.statistics.ChessGameStatistics;
import chess.chessgame.repository.ChessGameManagerRepository;
import org.springframework.stereotype.Service;

@Service
public class ChessServiceImpl implements ChessService {
    private final ChessGameManagerRepository chessGameManagerRepository;

    public ChessServiceImpl(ChessGameManagerRepository chessGameManagerRepository) {
        this.chessGameManagerRepository = chessGameManagerRepository;
    }

    @Override
    public ChessGameManager load(long gameId) {
        return chessGameManagerRepository.findById(gameId);
    }

    @Override
    public void move(long gameId, Color userColor, Position from, Position to) {
        if (!nextColor(gameId).equals(userColor)) {
            throw new IllegalStateException("내가 움직일 차례가 아닙니다.");
        }
        ChessGameManager chessGameManager = chessGameManagerRepository.findById(gameId);
        chessGameManager.move(from, to);
        if (chessGameManager.isKingDead()) {
            chessGameManager = chessGameManager.end();
        }
        chessGameManagerRepository.update(chessGameManager);
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
