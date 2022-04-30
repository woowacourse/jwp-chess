package chess.dao;

import chess.domain.position.Position;
import chess.service.dto.PieceDto;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcPieceDao implements PieceDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcPieceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void remove(final int id, final Position position) {
        final String sql = "delete from piece where game_id = ? and position = ?";
        jdbcTemplate.update(sql, id, position.getName());
    }

    @Override
    public void removeAll(final int id) {
        final String sql = "delete from piece where game_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void saveAll(final int id, final List<PieceDto> pieceDtos) {
        final String sql = "insert into piece (position, type, color, game_id) values (?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, getBatchPreparedStatementSetter(pieceDtos, id));
    }

    @Override
    public void save(final int id, final PieceDto pieceDto) {
        final String sql = "insert into piece (position, type, color, game_id) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, pieceDto.getPosition(), pieceDto.getType(), pieceDto.getColor(), id);
    }

    @Override
    public List<PieceDto> findAll(final int id) {
        final String sql = "select * from piece";
        return jdbcTemplate.query(sql, getPieceDtoRowMapper());
    }

    @Override
    public void modifyPosition(final int id, final Position source, final Position target) {
        final String sql = "update piece set position = ? where game_id = ? and position = ?";
        jdbcTemplate.update(sql, target.getName(), id, source.getName());
    }

    private BatchPreparedStatementSetter getBatchPreparedStatementSetter(final List<PieceDto> pieceDtos, final int id) {
        return new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                PieceDto pieceDto = pieceDtos.get(i);
                ps.setString(1, pieceDto.getPosition());
                ps.setString(2, pieceDto.getType());
                ps.setString(3, pieceDto.getColor());
                ps.setInt(4, id);
            }

            @Override
            public int getBatchSize() {
                return pieceDtos.size();
            }
        };
    }

    private RowMapper<PieceDto> getPieceDtoRowMapper() {
        return (resultSet, rowNum) ->
                PieceDto.of(
                        resultSet.getString("position"),
                        resultSet.getString("color"),
                        resultSet.getString("type")
                );
    }
}
