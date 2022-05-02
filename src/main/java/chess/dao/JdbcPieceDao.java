package chess.dao;

import chess.dao.entity.PieceEntity;
import chess.domain.position.Position;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcPieceDao implements PieceDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcPieceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void removeByPosition(Long gameId, Position position) {
        final String sql = "delete from piece where game_id = ? and position = ?";
        jdbcTemplate.update(sql, gameId, position.getName());
    }

    @Override
    public void removeAll() {
        final String sql = "delete from piece";
        jdbcTemplate.update(sql);
    }

    @Override
    public void save(PieceEntity piece) {
        final String sql = "insert into piece (position, type, color, game_id) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, piece.getPosition(), piece.getType(), piece.getColor(), piece.getGameId());
    }

    @Override
    public void saveAll(List<PieceEntity> pieces) {
        final String sql = "insert into piece (position, type, color, game_id) values (?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                PieceEntity piece = pieces.get(i);
                ps.setString(1, piece.getPosition());
                ps.setString(2, piece.getType());
                ps.setString(3, piece.getColor());
                ps.setLong(4, piece.getGameId());
            }

            @Override
            public int getBatchSize() {
                return pieces.size();
            }
        });
    }

    @Override
    public List<PieceEntity> findPiecesByGameId(Long gameId) {
        final String sql = "select * from piece where game_id = ?";
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) ->
                        new PieceEntity(
                                resultSet.getLong("id"),
                                resultSet.getString("position"),
                                resultSet.getString("type"),
                                resultSet.getString("color"),
                                resultSet.getLong("game_id")
                        ),
                gameId
        );
    }

    @Override
    public void updatePosition(Long gameId, Position position, Position updatedPosition) {
        final String sql = "update piece set position = ? where game_id = ? and position = ?";
        jdbcTemplate.update(sql, updatedPosition.getName(), gameId, position.getName());
    }
}
