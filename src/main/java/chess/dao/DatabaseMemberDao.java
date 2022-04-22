package chess.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import chess.domain.Member;

@Repository
public class DatabaseMemberDao implements MemberDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Long save(Member member) {
        final String sql = "insert into Member (name) values (?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, member.getName());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public Optional<Member> findById(Long id) {
        final String sql = "select id, name from Member where id = ?";
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(sql,
                        (resultSet, rowNum) -> new Member(
                                resultSet.getLong("id"),
                                resultSet.getString("name")
                        ), id)
        );
    }

    @Override
    public List<Member> findAll() {
        final String sql = "select id, name from Member";
        return jdbcTemplate.query(
                sql, (resultSet, rowNum) -> new Member(
                        resultSet.getLong("id"),
                        resultSet.getString("name")
                ));
    }

    @Override
    public void deleteById(Long id) {
        final String sql = "delete from Member where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
