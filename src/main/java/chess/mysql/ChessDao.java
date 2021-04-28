package chess.mysql;

import java.util.List;
import java.util.Optional;

public interface ChessDao {
    ChessGameDto save(ChessGameDto entity);

    void update(ChessGameDto entity);

    void delete(long id);

    Optional<ChessGameDto> findById(long id);

    List<ChessGameDto> findAllOnRunning();
}
