package chess.dao;

import chess.domain.game.ChessBoard;
import chess.domain.member.Member;
import chess.domain.pieces.Color;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class WebChessBoardDao implements BoardDao<ChessBoard> {

    private final MemberDao<Member> memberDao;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public WebChessBoardDao(MemberDao<Member> memberDao,
                            NamedParameterJdbcTemplate jdbcTemplate) {
        this.memberDao = memberDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ChessBoard save(ChessBoard board) {
        final String sql = "INSERT INTO board (room_title, turn, password) VALUES (:room_title, :turn, :password)";

        List<String> keys = List.of("room_title", "turn", "password");
        List<Object> values = List.of(board.getRoomTitle(), board.getTurn().name(), board.getPassword());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql, ParameterSourceCreator.makeParameterSource(keys, values), keyHolder);
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        return new ChessBoard(id, board.getRoomTitle(), board.getTurn());
    }

    @Override
    public Optional<ChessBoard> findById(int id) {
        final String sql = "SELECT * FROM board WHERE id=:id";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
        final ChessBoard chessBoard = DataAccessUtils.singleResult(jdbcTemplate.query(sql, namedParameters, (rs, rowNum) -> makeBoard(rs)));
        return Optional.ofNullable(chessBoard);
    }

    @Override
    public List<ChessBoard> findAll() {
        final String sql = "SELECT * FROM board";
        return jdbcTemplate.query(sql, new MapSqlParameterSource(), (rs, rowNum) -> makeBoard(rs));
    }

    @Override
    public int deleteByIdAndPassword(int id, String password) {
        String sql = "DELETE FROM board where id=:id and password=:password";
        List<String> keys = List.of("id", "password");
        List<Object> values = List.of(id, password);
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
                memberDao.getAllByBoardId(resultSet.getInt("id")),
                resultSet.getString("password"));
    }
}
