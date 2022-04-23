package chess.dao.springjdbc;

import chess.dao.GameDao;
import chess.service.dto.ChessGameDto;
import chess.service.dto.GamesDto;
import chess.service.dto.StatusDto;
import java.sql.PreparedStatement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class SpringGameDao implements GameDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ChessGameDto> chessGameDtoRowMapper = (resultSet, rowNum) -> new ChessGameDto(
        resultSet.getInt("id"),
        resultSet.getString("name"),
        resultSet.getString("status"),
        resultSet.getString("turn")
    );

    public SpringGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void update(ChessGameDto dto) {
        String sql = "UPDATE game SET status = ?, turn = ? WHERE id = ?";
        jdbcTemplate.update(sql, dto.getStatus(), dto.getTurn(), dto.getId());
    }

    @Override
    public ChessGameDto findById(int id) {
        String sql = "SELECT id, name, status, turn FROM game WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, chessGameDtoRowMapper, id);
    }

    @Override
    public void updateStatus(StatusDto statusDto, int id) {
        String sql = "UPDATE game SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, statusDto.getStatus(), id);
    }

    @Override
    public GamesDto findAll() {
        String sql = "SELECT id, name, status, turn FROM game";
        return new GamesDto(jdbcTemplate.query(sql, chessGameDtoRowMapper));
    }

    @Override
    public int createGame(String name) {
        String sql = "INSERT INTO game SET name = ?";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement prepareStatement = con.prepareStatement(sql, new String[]{"id"});
            prepareStatement.setString(1, name);
            return prepareStatement;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }
}
