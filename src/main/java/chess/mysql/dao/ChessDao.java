package chess.mysql.dao;

import chess.mysql.dao.dto.ChessGameDto;

import java.util.List;
import java.util.Optional;

public interface ChessDao {
    long save(ChessGameDto entity);

    void update(ChessGameDto entity);

    void delete(long id);

    Optional<ChessGameDto> findById(long id);

    List<ChessGameDto> findAllOnRunning();
}
