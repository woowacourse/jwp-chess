package chess.dao;

import chess.entity.Chess;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChessDao {

    void save(final Chess chess);

    Optional<Chess> findByName(final String name);

    void update(final Chess chess);

    void deleteByName(final String name);
}
