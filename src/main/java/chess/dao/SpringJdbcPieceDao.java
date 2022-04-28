package chess.dao;

import chess.domain.position.Position;
import chess.dto.PieceDto;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SpringJdbcPieceDao implements PieceDao {

    private final JdbcTemplate jdbcTemplate;

    public SpringJdbcPieceDao(JdbcTemplate jdbcTemplate) {
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
        final String sql = "insert into piece (position, type, color, game_id) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, pieceDto.getPosition(), pieceDto.getType(), pieceDto.getColor(), pieceDto.getGameId());
    }

    @Override
    public void saveAll(List<PieceDto> pieceDtos) {
        final String sql = "insert into piece (position, type, color, game_id) values (?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                PieceDto pieceDto = pieceDtos.get(i);
                ps.setString(1, pieceDto.getPosition());
                ps.setString(2, pieceDto.getType());
                ps.setString(3, pieceDto.getColor());
                ps.setLong(4, pieceDto.getGameId());}

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
    public List<PieceDto> findPiecesById(Long gameId) {
        final String sql = "select * from piece where game_id = ?";
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) ->
                        new PieceDto(
                                resultSet.getString("position"),
                                resultSet.getString("color"),
                                resultSet.getString("type")
                        ),
                gameId
        );
    }

    @Override
    public void updatePosition(Position position, Position updatedPosition) {
        final String sql = "update piece set position = ? where position = ?";
        jdbcTemplate.update(sql, updatedPosition.getName(), position.getName());
    }
}
