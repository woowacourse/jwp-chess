package chess.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import chess.dao.ChessGameDao;
import chess.dao.SquareDao;
import chess.dao.StateDao;
import chess.dto.ChessGameDto;
import chess.entity.ChessGameEntity;
import chess.model.board.Board;
import chess.model.state.State;

@Repository
public class GamesRepositoryImpl implements GamesRepository {

    private final ChessGameDao chessGameDao;
    private final SquareDao squareDao;
    private final StateDao stateDao;

    public GamesRepositoryImpl(ChessGameDao chessGameDao, SquareDao squareDao, StateDao stateDao) {
        this.chessGameDao = chessGameDao;
        this.squareDao = squareDao;
        this.stateDao = stateDao;
    }

    @Override
    public void save(ChessGameDto chessGameDto) {
        chessGameDao.insert(chessGameDto);
    }

    @Override
    public List<ChessGameEntity> getGames() {
        return chessGameDao.findAll();
    }

    @Override
    public void delete(Long id) {
        chessGameDao.delete(id);
        squareDao.delete(id);
        stateDao.delete(id);
    }

    @Override
    public ChessGameEntity getGame(Long id) {
        return chessGameDao.find(id);
    }

    public State getState(Long id) {
        Board board = squareDao.createBoard(id);
        return stateDao.find(id, board);
    }
}
