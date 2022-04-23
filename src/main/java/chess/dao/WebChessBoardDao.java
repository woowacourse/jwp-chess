package chess.dao;

import chess.domain.game.ChessBoard;
import chess.domain.pieces.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class WebChessBoardDao implements BoardDao<ChessBoard> {

    @Autowired
    private WebChessMemberDao webChessMemberDao;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public WebChessBoardDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ChessBoard save(ChessBoard board) {
        final String sql = "INSERT INTO board (room_title, turn) VALUES (:room_title, :turn)";

        List<String> keys = List.of("room_title", "turn");
        List<Object> values = List.of(board.getRoomTitle(), board.getTurn().name());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql, ParameterSourceCreator.makeParameterSource(keys, values), keyHolder);
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        return new ChessBoard(id, board.getRoomTitle(), board.getTurn());
    }

    @Override
    public ChessBoard getById(int id) {
        final String sql = "SELECT * FROM board WHERE id=:id";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
        return jdbcTemplate.queryForObject(sql, namedParameters,
                (rs, rowNum) -> makeBoard(rs));
    }

    @Override
    public List<ChessBoard> findAll() {
        final String sql = "SELECT * FROM board";
        return jdbcTemplate.query(sql, new MapSqlParameterSource(), (rs, rowNum) -> makeBoard(rs));
    }

    @Override
    public int deleteById(int id) {
        String sql = "DELETE FROM board where id=:id";
        List<String> keys = List.of("id");
        List<Object> values = List.of(id);
        return jdbcTemplate.update(sql, ParameterSourceCreator.makeParameterSource(keys, values));
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM board";
        jdbcTemplate.update(sql, new MapSqlParameterSource());
    }

    @Override
    public void updateTurn(Color color, int id) {
        String sql = "UPDATE board SET turn=:turn WHERE id=:id";
        List<String> keys = List.of("turn", "id");
        List<Object> values = List.of(color.name(), id);
        jdbcTemplate.update(sql, ParameterSourceCreator.makeParameterSource(keys, values));
    }

    private ChessBoard makeBoard(ResultSet resultSet) throws SQLException {
        return new ChessBoard(
                resultSet.getInt("id"),
                resultSet.getString("room_title"),
                Color.findColor(resultSet.getString("turn")),
                webChessMemberDao.getAllByBoardId(resultSet.getInt("id")));
    }
}
