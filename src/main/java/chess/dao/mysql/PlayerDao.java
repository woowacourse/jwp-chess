package chess.dao.mysql;

import org.springframework.stereotype.Component;

import chess.dao.connect.CustomJdbcTemplate;
import chess.dao.dto.PlayerDto;

@Component
public class PlayerDao {

    private final CustomJdbcTemplate customJdbcTemplate;

    public PlayerDao(final CustomJdbcTemplate customJdbcTemplate) {
        this.customJdbcTemplate = customJdbcTemplate;
    }


    public Long save(final PlayerDto playerDto) {
        final String query = "INSERT INTO Player (color, pieces) VALUES (?,?)";
        return customJdbcTemplate.executeInsertQuery(query,
                preparedStatement -> {
                    preparedStatement.setString(1, playerDto.getColorName());
                    preparedStatement.setString(2, playerDto.getPieces());
                });
    }

    public PlayerDto findById(final Long id) {
        final String query = "SELECT id, color, pieces FROM Player where id=?";
        return customJdbcTemplate.executeQuery(query,
                preparedStatement -> preparedStatement.setLong(1, id),
                resultSet -> new PlayerDto(
                        resultSet.getLong("id"),
                        resultSet.getString("color"),
                        resultSet.getString("pieces")
                ));
    }

    public void update(final PlayerDto playerDto) {
        final String query = "UPDATE Player SET pieces=? WHERE id=?";
        customJdbcTemplate.executeQuery(query,
                preparedStatement -> {
                    preparedStatement.setString(1, playerDto.getPieces());
                    preparedStatement.setLong(2, playerDto.getId());
                });
    }

    public void remove(final long id) {
        final String query = "DELETE FROM Player WHERE id=?";
        customJdbcTemplate.executeQuery(query,
                preparedStatement -> {
                    preparedStatement.setLong(1, id);
                });
    }
}
