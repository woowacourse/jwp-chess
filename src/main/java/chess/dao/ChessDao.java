package chess.dao;

import chess.entity.Chess;
import java.util.List;
import java.util.Optional;

public interface ChessDao {

    void save(final Chess chess);

    Optional<Chess> findByName(final String name);

    void update(final Chess chess);

    void deleteByName(final String name);

    List<Chess> find();
}
