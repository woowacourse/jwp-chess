package chess.dao;

import chess.domain.position.Position;
import chess.dto.PieceDto;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PieceDaoSpringImpl implements PieceDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public PieceDaoSpringImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void remove(Position position) {
        final String sql = "delete from piece where position = ?";
        jdbcTemplate.update(sql, position.getName());
    }

    @Override
    public void removeAll() {
        final String sql = "delete from piece";
        jdbcTemplate.update(sql);
    }

    @Override
    public void saveAll(List<PieceDto> pieceDtos) {
        final String sql = "insert into piece (position, type, color) values (?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                PieceDto pieceDto = pieceDtos.get(i);
                ps.setString(1, pieceDto.getPosition());
                ps.setString(2, pieceDto.getType());
                ps.setString(3, pieceDto.getColor());
            }

            @Override
            public int getBatchSize() {
                return pieceDtos.size();
            }
        });
    }

    @Override
    public void save(PieceDto pieceDto) {
        final String sql = "insert into piece (position, type, color) values (?, ?, ?)";
        jdbcTemplate.update(sql, pieceDto.getPosition(), pieceDto.getType(), pieceDto.getColor());
    }

    @Override
    public List<PieceDto> findAll() {
        final String sql = "select * from piece";
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) ->
                        new PieceDto(
                                resultSet.getString("position"),
                                resultSet.getString("color"),
                                resultSet.getString("type")
                        )
        );
    }

    @Override
    public void update(Position source, Position target) {
        final String sql = "update piece set position = ? where position = ?";
        jdbcTemplate.update(sql, target.getName(), source.getName());
    }
}
