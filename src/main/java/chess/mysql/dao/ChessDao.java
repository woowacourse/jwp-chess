package chess.mysql.dao;

import chess.mysql.dao.dto.ChessGame;

import java.util.List;
import java.util.Optional;

public interface ChessDao {
    long save(ChessGame entity);

    void update(ChessGame entity);

    void delete(long id);

    Optional<ChessGame> findById(long id);

    List<ChessGame> findAllOnRunning();
}
