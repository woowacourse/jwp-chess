package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberDaoTest {

    final MockMemberDao memberDao = new MockMemberDao();

    @BeforeEach
    void beforeEach() {
        memberDao.deleteAll();
    }

    @Test
    @DisplayName("멤버를 저장소에 저장한다.")
    void save() {
        final MockMemberDao repository = new MockMemberDao();
        final Member member = new Member("alex");
        final Long memberId = repository.save(member);
        assertThat(repository.findById(memberId).get().getName()).isEqualTo(member.getName());
    }

    @Test
    @DisplayName("저장소에 저장된 모든 멤버를 불러온다.")
    void findAll() {
        final MockMemberDao repository = new MockMemberDao();
        final List<String> memberNames = new ArrayList<>();

        memberNames.add("alex");
        memberNames.add("eve");
        memberNames.add("alien");
        memberNames.add("baekara");
        memberNames.add("sun");
        memberNames.add("corinne");

        for (final String memberName : memberNames) {
            repository.save(new Member(memberName));
        }

        final List<String> stored = repository.findAll()
                .stream()
                .map(Member::getName)
                .collect(Collectors.toList());

        assertThat(stored).isEqualTo(memberNames);
    }
}
