package chess.dao;

import chess.domain.member.Member;
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
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("board_id", boardId);
        SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, namedParameters);

        List<Member> members = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            int id = (int) map.get("id");
            String name = (String) map.get("name");

            members.add(new Member(id, name, boardId));
        }
        return members;
    }

    @Override
    public Member save(String name, int boardId) {
        final String sql = "INSERT INTO member(name, board_id) VALUES (:name, :board_id)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("board_id", boardId);
        SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
        jdbcTemplate.update(sql, namedParameters, keyHolder);
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();

        return new Member(id, name, boardId);
    }
}
