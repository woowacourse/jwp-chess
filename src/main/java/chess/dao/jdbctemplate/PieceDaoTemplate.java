package chess.dao.jdbctemplate;

import chess.dao.PieceDao;
import chess.dao.dto.piece.PieceDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PieceDaoTemplate implements PieceDao {

    private final JdbcTemplate jdbcTemplate;

    public PieceDaoTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<PieceDto> actorRowMapper = (resultSet, rowNum) ->
            new PieceDto(
                    resultSet.getLong("id"),
                    resultSet.getLong("game_id"),
                    resultSet.getString("symbol"),
                    resultSet.getString("position")
            );

    @Override
    public Long save(PieceDto pieceDto) {
        String sql = "INSERT INTO piece(game_id, position, symbol) VALUES (?, ?, ?)";
        return (long) jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, pieceDto.getGameId());
            ps.setString(2, pieceDto.getPosition());
            ps.setString(3, pieceDto.getSymbol());
            return ps;
        }, new GeneratedKeyHolder());
    }

    @Override
    public long[] savePieces(final Long gameId, final List<PieceDto> pieces) {
        String sql = "INSERT INTO piece(game_id, position, symbol) VALUES (?, ?, ?)";
        List<Object[]> collect = pieces.stream()
                .map(pieceDto -> new Object[]{ gameId, pieceDto.getPosition(), pieceDto.getSymbol() })
                .collect(Collectors.toList());
        int[] ints = jdbcTemplate.batchUpdate(sql, collect);
        return Arrays.stream(ints).asLongStream().toArray();
    }

    @Override
    public Long updateByGameIdAndPosition(PieceDto pieceDto) {
        String sql = "UPDATE piece SET symbol = ? WHERE game_id=? AND position=?";
        return (long) jdbcTemplate.update(sql, pieceDto.getSymbol(), pieceDto.getGameId(), pieceDto.getPosition());
    }

    @Override
    public List<PieceDto> findPiecesByGameId(final Long gameId) {
        String sql = "SELECT * from piece where game_id = ?";
        return jdbcTemplate.query(sql, actorRowMapper, gameId);
    }

    @Override
    public PieceDto findById(Long id) {
        String sql = "SELECT * from piece where id = ?";
        return jdbcTemplate.queryForObject(sql, actorRowMapper, id);
    }
}
