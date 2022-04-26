package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dao.member.MemberDao;
import chess.dao.member.SpringJdbcMemberDao;
import chess.domain.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class MemberDaoTest {

    private MemberDao memberDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void beforeEach() {
        memberDao = new SpringJdbcMemberDao(jdbcTemplate);
    }

    @Test
    @DisplayName("멤버를 저장소에 저장한다.")
    void save() {
        final Member member = new Member("alex");
        final Long memberId = memberDao.save(member);
        assertThat(memberDao.findById(memberId).get().getName()).isEqualTo(member.getName());
    }

    @Test
    @DisplayName("저장소에 저장된 모든 멤버를 불러온다.")
    void findAll() {
        final List<String> memberNames = new ArrayList<>();

        memberNames.add("alex");
        memberNames.add("eve");
        memberNames.add("alien");
        memberNames.add("baekara");
        memberNames.add("sun");
        memberNames.add("corinne");

        for (final String memberName : memberNames) {
            memberDao.save(new Member(memberName));
        }

        final List<String> stored = memberDao.findAll()
                .stream()
                .map(Member::getName)
                .collect(Collectors.toList());

        assertThat(stored).isEqualTo(memberNames);
    }
}
