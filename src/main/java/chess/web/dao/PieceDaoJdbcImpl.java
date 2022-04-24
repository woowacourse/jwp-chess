package chess.web.dao;

import chess.web.dto.PieceDto;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PieceDaoJdbcImpl implements PieceDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<PieceDto> pieceDtoRowMapper = (resultSet, rowNum) -> {
        return new PieceDto(
                resultSet.getString("piece_type"),
                resultSet.getString("position"),
                resultSet.getString("color")
        );
    };

    public PieceDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(PieceDto pieceDto) {
        final String sql = "insert into piece (piece_type, position, color) values (?, ?, ?)";
        jdbcTemplate.update(sql, pieceDto.getPieceType(), pieceDto.getPosition(), pieceDto.getColor());
    }

    @Override
    public void deleteAll() {
        final String sql = "delete from piece";
        jdbcTemplate.update(sql);
    }

    @Override
    public void update(PieceDto pieceDto) {
        final String sql = "update piece set piece_type=?, position=?, color=? where position=?";
        jdbcTemplate.update(sql,
                pieceDto.getPieceType(), pieceDto.getPosition(), pieceDto.getColor(), pieceDto.getPosition());
    }

    @Override
    public List<PieceDto> findAll() {
        final String sql = "select * from piece";
        return jdbcTemplate.query(sql, pieceDtoRowMapper);
    }
}
