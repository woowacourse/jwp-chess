package chess.repository;

import chess.model.Color;
import chess.model.board.Board;
import chess.model.game.ChessGame;
import chess.model.game.Status;
import chess.repository.dao.GameDao;
import chess.repository.dao.entity.GameEntity;
import org.springframework.stereotype.Repository;

@Repository
public class ChessGameRepository {

    private final GameDao gameDao;
    private final BoardRepository boardRepository;

    public ChessGameRepository(final GameDao gameDao, final BoardRepository boardRepository) {
        this.gameDao = gameDao;
        this.boardRepository = boardRepository;
    }

    public ChessGame findById(final Integer id) {
        GameEntity gameEntity = gameDao.findById(id);
        return convertEntityToChessGame(gameEntity);
    }

    private ChessGame convertEntityToChessGame(final GameEntity entity) {
        Color turn = Color.valueOf(entity.getTurn());
        Status status = Status.findByGameEntity(entity.getStatus());
        Board board = boardRepository.findById(entity.getId());
        return new ChessGame(turn, status, board);
    }

    public int update(final Integer gameId, final ChessGame chessGame) {
        GameEntity gameEntity = new GameEntity(gameId, chessGame);
        return gameDao.update(gameEntity);
    }
}
