package chess.dao;

import chess.domain.member.Member;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class WebChessMemberDao implements MemberDao<Member> {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public WebChessMemberDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveAll(List<Member> members, int boardId) {
        for (Member member : members) {
            save(member.getName(), boardId);
        }
    }

    @Override
    public List<Member> getAllByBoardId(int boardId) {
        final String sql = "SELECT * FROM member WHERE board_id=:board_id";

        List<String> keys = List.of("board_id");
        List<Object> values = List.of(boardId);
        SqlParameterSource parameterSource = ParameterSourceCreator.makeParameterSource(keys, values);

        return jdbcTemplate.query(sql, parameterSource, (rs, rowNum) -> makeMember(rs));
    }

    private Member makeMember(ResultSet rs) throws SQLException {
        return new Member(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("board_id")
        );
    }

    @Override
    public Member save(String name, int boardId) {
        final String sql = "INSERT INTO member(name, board_id) VALUES (:name, :board_id)";

        List<String> keys = List.of("name", "board_id");
        List<Object> values = List.of(name, boardId);
        SqlParameterSource parameterSource = ParameterSourceCreator.makeParameterSource(keys, values);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, parameterSource, keyHolder);

        return new Member(Objects.requireNonNull(keyHolder.getKey()).intValue(), name, boardId);
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM member";
        jdbcTemplate.update(sql, new MapSqlParameterSource());
    }
}
