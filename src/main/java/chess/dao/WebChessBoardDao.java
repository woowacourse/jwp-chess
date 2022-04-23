package chess.dao;

import chess.domain.game.ChessBoard;
import chess.domain.member.Member;
import chess.domain.pieces.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class WebChessBoardDao implements BoardDao<ChessBoard> {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public WebChessBoardDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public ChessBoard save(ChessBoard board) {
        final String sql = "INSERT INTO board (room_title, turn) VALUES (:room_title, :turn)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("room_title", board.getRoomTitle());
        parameters.put("turn", board.getTurn().name());
        SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
        jdbcTemplate.update(sql, namedParameters, keyHolder);
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
                (rs, rowNum) -> makeBoard(rs, new ChessMemberDao(new ChessConnectionManager())));
    }

    private ChessBoard makeBoard(ResultSet resultSet, ChessMemberDao chessMemberDao) throws SQLException {
        return new ChessBoard(
                resultSet.getInt("id"),
                resultSet.getString("room_title"),
                Color.findColor(resultSet.getString("turn")),
                chessMemberDao.getAllByBoardId(resultSet.getInt("id")));
    }

    @Override
    public List<ChessBoard> findAll() {
        final String sql = "SELECT * FROM board";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, new MapSqlParameterSource());
        List<ChessBoard> boards = new ArrayList<>();
        MemberDao<Member> memberDao = new ChessMemberDao(new ChessConnectionManager());
        for (Map<String, Object> map : maps) {
            int id = (int) map.get("id");
            String roomTitle = (String) map.get("room_title");
            String turn = (String) map.get("turn");
            List<Member> members = memberDao.getAllByBoardId((int) map.get("id"));

            boards.add(new ChessBoard(id, roomTitle, Color.findColor(turn), members));
        }
        return boards;
    }

    @Override
    public int deleteById(int id) {
        String sql = "DELETE FROM board where id=:id";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
        return jdbcTemplate.update(sql, namedParameters);
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM board";
        jdbcTemplate.update(sql, new MapSqlParameterSource());
    }

    @Override
    public void updateTurn(Color color, int id) {
        String sql = "UPDATE board SET turn=:turn WHERE id=:id";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("turn", color.name());
        parameters.put("id", id);
        SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
        jdbcTemplate.update(sql, namedParameters);
    }
}
