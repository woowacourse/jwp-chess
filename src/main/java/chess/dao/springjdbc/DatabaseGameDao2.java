package chess.dao.springjdbc;

import chess.dao.GameDao;
import chess.dao.jdbc.jdbcutil.JdbcUtil;
import chess.dao.jdbc.jdbcutil.StatementExecutor;
import chess.service.dto.ChessGameDto;
import chess.service.dto.GamesDto;
import chess.service.dto.StatusDto;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class DatabaseGameDao2 implements GameDao {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String STATUS = "status";
    private static final String TURN = "turn";

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ChessGameDto> chessGameDtoRowMapper = (resultSet, rowNum) -> new ChessGameDto(
        resultSet.getInt("id"),
        resultSet.getString("name"),
        resultSet.getString("status"),
        resultSet.getString("turn")
    );

    public DatabaseGameDao2(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void update(ChessGameDto dto) {
        String sql = "update game set status = ?, turn = ? where id = ?";
        new StatementExecutor(JdbcUtil.getConnection(), sql)
            .setString(dto.getStatus())
            .setString(dto.getTurn())
            .setInt(dto.getId())
            .executeUpdate();
    }

    @Override
    public ChessGameDto findById(int id) {
        String sql = "select id, name, status, turn from game where id = ?";
        return new StatementExecutor(JdbcUtil.getConnection(), sql)
            .setInt(id)
            .findFirst(this::getChessGameDto);
    }

    private ChessGameDto getChessGameDto(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(ID);
        String name = resultSet.getString(NAME);
        String status = resultSet.getString(STATUS);
        String turn = resultSet.getString(TURN);
        return new ChessGameDto(id, name, status, turn);
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
    public void createGame(String name) {
        String sql = "INSERT INTO game SET name = ?";
        jdbcTemplate.update(sql, name);
    }
}
