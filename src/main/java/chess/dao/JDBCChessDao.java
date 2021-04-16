package chess.dao;

import chess.entity.Chess;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JDBCChessDao implements ChessDao {
    @Override
    public void save(Chess chess) {

    }

    @Override
    public Optional<Chess> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public void update(Chess chess) {

    }

    @Override
    public void deleteByName(String name) {

    }
}
