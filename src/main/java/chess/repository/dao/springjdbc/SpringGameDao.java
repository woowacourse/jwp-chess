package chess.repository.dao.springjdbc;

import chess.repository.dao.GameDao;
import chess.repository.dao.entity.GameEntity;
import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class SpringGameDao implements GameDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<GameEntity> chessGameDtoRowMapper = (resultSet, rowNum) ->
            new GameEntity(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("status"),
                    resultSet.getString("turn"),
                    resultSet.getString("password")
            );

    public SpringGameDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int update(final GameEntity game) {
        String sql = "UPDATE game SET status = ?, turn = ? WHERE id = ?";
        return jdbcTemplate.update(sql, game.getStatus(), game.getTurn(), game.getId());
    }

    @Override
    public GameEntity findById(final Integer id) {
        String sql = "SELECT id, name, status, turn, password FROM game WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, chessGameDtoRowMapper, id);
    }

    @Override
    public List<GameEntity> findAll() {
        String sql = "SELECT id, name, status, turn, password FROM game";
        return jdbcTemplate.query(sql, chessGameDtoRowMapper);
    }

    @Override
    public int createGame(final String name, final String password) {
        String sql = "INSERT INTO game SET name = ?, password = ?";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement prepareStatement = con.prepareStatement(sql, new String[]{"id"});
            prepareStatement.setString(1, name);
            prepareStatement.setString(2, password);
            return prepareStatement;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    @Override
    public int deleteGame(final Integer id) {
        String sql = "DELETE FROM game where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public String findPasswordById(Integer gameId) {
        String sql = "SELECT password FROM game WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, gameId);
    }
}
