package chess.dao;

import chess.domain.position.Position;
import chess.dto.PieceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PieceDaoImpl implements PieceDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PieceDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void removeByPosition(Position position) {
        final String sql = "delete from piece where position = ?";
        jdbcTemplate.update(sql, position.getName());
    }

    @Override
    public void removeAll() {
        final String sql = "delete from piece";
        jdbcTemplate.update(sql);
    }

    @Override
    public void save(PieceDto pieceDto) {
        final String sql = "insert into piece (position, type, color) values (?, ?, ?)";
        jdbcTemplate.update(sql, pieceDto.getPosition(), pieceDto.getType(), pieceDto.getColor());
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
    public void updatePosition(Position position, Position updatedPosition) {
        final String sql = "update piece set position = ? where position = ?";
        jdbcTemplate.update(sql, updatedPosition.getName(), position.getName());
    }
}
