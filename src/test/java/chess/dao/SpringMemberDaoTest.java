package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.domain.Member;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@TestPropertySource("classpath:application-test.properties")
@Sql("classpath:init.sql")
class SpringMemberDaoTest {

    private final MemberDao memberDao;

    @Autowired
    private SpringMemberDaoTest(final JdbcTemplate jdbcTemplate) {
        this.memberDao = new ChessMemberDao(jdbcTemplate);
    }

    @DisplayName("정상적으로 멤버가 등록되는지 확인한다.")
    @Test
    void saveMember() {
        memberDao.save(new Member(1L, "abc"));
        final Optional<Member> member = memberDao.findById(1L);

        assertAll(
                () -> assertThat(member).isNotNull(),
                () -> assertThat(member.get().getName()).isEqualTo("abc")
        );
    }

    @DisplayName("모든 멤버 불러오기가 가능한지 확인한다.")
    @Test
    void findAllMembers() {
        memberDao.save(new Member(1L, "썬"));
        memberDao.save(new Member(2L, "문"));

        assertThat(memberDao.findAll().size()).isEqualTo(2);
    }

    @DisplayName("모든 멤버 불러오기가 가능한지 확인한다.")
    @Test
    void deleteMember() {
        memberDao.save(new Member(1L, "썬"));
        memberDao.deleteById(1L);

        assertThat(memberDao.findAll().size()).isEqualTo(0);
    }
}

