package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@Sql("classpath:init.sql")
class SpringMemberDaoTest {

    @Autowired
    private MemberDao memberDao;

    @DisplayName("정상적으로 멤버가 등록되는지 확인한다.")
    @Test
    void saveMember() {
        memberDao.save(new Member(1L, "abc"));

        assertThat(memberDao.findById(1L).get().getName()).isEqualTo("abc");
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

