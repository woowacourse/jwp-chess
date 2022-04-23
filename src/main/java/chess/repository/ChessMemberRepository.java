package chess.repository;

import chess.model.member.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ChessMemberRepository implements MemberRepository<Member> {

    private final JdbcTemplate jdbcTemplate;

    public ChessMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Member> getRowMapper() {
        return (resultSet, rowNum) -> new Member(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getInt("room_id"));
        }
    @Override
    public List<Member> findMembersByRoomId(int roomId) {
        return jdbcTemplate.query("SELECT * FROM member WHERE room_id=?", getRowMapper(), roomId);
    }

    @Override
    public Member save(String name, int roomId) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("room_id", roomId);
        Number number = simpleJdbcInsert.executeAndReturnKey(parameters);

        return new Member(number.intValue(), name, roomId);
    }

    @Override
    public void saveAll(List<Member> members, int roomId) {
        for (Member member : members) {
            save(member.getName(), roomId);
        }
    }
}
