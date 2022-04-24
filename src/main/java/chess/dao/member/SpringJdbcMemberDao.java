package chess.dao.member;

import chess.domain.Member;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class SpringJdbcMemberDao implements MemberDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Optional<Member>> memberRowMapper = (resultSet, rowNumber) -> Optional.of(
            new Member(resultSet.getLong("id"), resultSet.getString("name"))
    );

    private final RowMapper<List<Member>> membersRowMapper = (resultSet, rowNumber) -> {
        List<Member> members = new ArrayList<>();
        while (!resultSet.isAfterLast()) {
            members.add(new Member(resultSet.getLong("id"), resultSet.getString("name")));
            resultSet.next();
        }
        return members;
    };

    @Autowired
    public SpringJdbcMemberDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(final Member member) {
        final String sql = "insert into Member (name) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            final PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, member.getName());
            return statement;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public Optional<Member> findById(final Long id) {
        final String sql = "select id, name from Member where id = ?";
        return jdbcTemplate.queryForObject(sql, memberRowMapper, id);
    }

    @Override
    public List<Member> findAll() {
        final String sql = "select id, name from Member";
        return jdbcTemplate.queryForObject(sql, membersRowMapper);
    }

    @Override
    public void deleteById(final Long id) {
        final String sql = "delete from Member where id = ?";
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            return statement;
        });
    }
}
