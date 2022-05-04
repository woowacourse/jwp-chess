package chess.dao.springjdbc;

import chess.dao.GameDao;
import chess.service.dto.ChessGameDto;
import chess.service.dto.GamesDto;
import chess.service.dto.StatusDto;
import java.sql.PreparedStatement;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class SpringGameDao implements GameDao {

    private static final RowMapper<ChessGameDto> chessGameDtoRowMapper = (resultSet, rowNum) -> new ChessGameDto(
        resultSet.getLong("id"),
        resultSet.getString("name"),
        resultSet.getString("status"),
        resultSet.getString("turn"),
        resultSet.getString("password")
    );

    private final JdbcTemplate jdbcTemplate;

    public SpringGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void update(ChessGameDto dto) {
        String sql = "UPDATE game SET status = ?, turn = ? WHERE id = ?";
        jdbcTemplate.update(sql, dto.getStatus(), dto.getTurn(), dto.getId());
    }

    @Override
    public ChessGameDto findById(Long id) {
        String sql = "SELECT id, name, status, turn, password FROM game WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, chessGameDtoRowMapper, id);
    }

    @Override
    public void updateStatus(StatusDto statusDto, Long id) {
        String sql = "UPDATE game SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, statusDto.getStatus(), id);
    }

    @Override
    public GamesDto findAll() {
        String sql = "SELECT id, name, status, turn, password FROM game";
        return new GamesDto(jdbcTemplate.query(sql, chessGameDtoRowMapper));
    }

    @Override
    public Long createGame(String name, String password) {
        String sql = "INSERT INTO game(name, password) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement prepareStatement = con.prepareStatement(sql, new String[]{"id"});
            prepareStatement.setString(1, name);
            prepareStatement.setString(2, password);
            return prepareStatement;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void deleteGame(Long id) {
        String sql = "DELETE FROM game WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM game where id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, id) != 0;
    }

    @Override
    public void removeAll() {
        String sql = "DELETE FROM game";
        jdbcTemplate.update(sql);
    }
}
