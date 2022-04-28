package chess.repository.mysql;

import java.sql.PreparedStatement;
import java.util.Objects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import chess.repository.PlayerDao;
import chess.repository.dto.player.PlayerDto;

@Component
public class MysqlPlayerDao implements PlayerDao {

    private final JdbcTemplate jdbcTemplate;

    public MysqlPlayerDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(final PlayerDto playerDto) {
        final String query = "INSERT INTO Player (color, pieces) VALUES (?,?)";
        final GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            final PreparedStatement preparedStatement = connection.prepareStatement(query, new String[]{"id"});
            preparedStatement.setString(1, playerDto.getColorName());
            preparedStatement.setString(2, playerDto.getPieces());
            return preparedStatement;
        }, generatedKeyHolder);
        return Objects.requireNonNull(generatedKeyHolder.getKey()).longValue();
    }

    @Override
    public PlayerDto findById(final Long id) {
        final String query = "SELECT id, color, pieces FROM Player where id=?";
        return jdbcTemplate.queryForObject(query,
                (resultSet, rowNum) -> new PlayerDto(
                        resultSet.getLong("id"),
                        resultSet.getString("color"),
                        resultSet.getString("pieces")
                ), id);
    }

    @Override
    public void update(final PlayerDto playerDto) {
        final String query = "UPDATE Player SET pieces=? WHERE id=?";
        jdbcTemplate.update(query, playerDto.getPieces(), playerDto.getId());
    }

    @Override
    public void remove(final Long id) {
        final String query = "DELETE FROM Player WHERE id=?";
        jdbcTemplate.update(query, id);
    }
}
