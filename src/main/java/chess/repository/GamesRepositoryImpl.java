package chess.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import chess.dao.ChessGameDao;
import chess.dto.ChessGameDto;
import chess.entity.ChessGameEntity;

@Repository
public class GamesRepositoryImpl implements GamesRepository {

    private final ChessGameDao chessGameDao;

    public GamesRepositoryImpl(ChessGameDao chessGameDao) {
        this.chessGameDao = chessGameDao;
    }

    @Override
    public void save(ChessGameDto chessGameDto) {
        chessGameDao.insert(chessGameDto);
    }

    @Override
    public List<ChessGameEntity> getGames() {
        return chessGameDao.findAll();
    }
}
