package chess.web.dao;

import chess.web.dto.board.PieceDto;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PieceDaoJdbcImpl implements PieceDao {

    private static final RowMapper<PieceDto> PIECE_DTO_ROW_MAPPER = (resultSet, rowNum) -> {
        return new PieceDto(
                resultSet.getString("piece_type"),
                resultSet.getString("position"),
                resultSet.getString("color")
        );
    };

    private final JdbcTemplate jdbcTemplate;

    public PieceDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(int gameId, PieceDto pieceDto) {
        final String sql = "insert into piece (game_id, piece_type, position, color) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, gameId, pieceDto.getPieceType(), pieceDto.getPosition(), pieceDto.getColor());
    }

    @Override
    public void updateByGameId(int gameId, PieceDto pieceDto) {
        final String sql = "update piece set piece_type = ?, position = ?, color = ? where game_id = ? and position = ?";
        jdbcTemplate.update(sql,
                pieceDto.getPieceType(), pieceDto.getPosition(), pieceDto.getColor(), gameId, pieceDto.getPosition());
    }

    @Override
    public List<PieceDto> findAllByGameId(int gameId) {
        final String sql = "select piece_type, position, color from piece where game_id = ?";
        return jdbcTemplate.query(sql, PIECE_DTO_ROW_MAPPER, gameId);
    }

    @Override
    public void deleteAllByGameId(int gameId) {
        final String sql = "delete from piece where game_id = ?";
        jdbcTemplate.update(sql, gameId);
    }
}