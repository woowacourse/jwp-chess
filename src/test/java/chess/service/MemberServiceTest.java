package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.dao.MockMemberDao;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberServiceTest {

    private final MockMemberDao mockMemberDao = new MockMemberDao();

    @BeforeEach
    void beforeEach() {
        mockMemberDao.deleteAll();
    }

    @DisplayName("저장소에 저장된 모든 멤버를 찾아 리스트로 반환한다.")
    @Test
    void findAllMembers() {
        final MemberService memberService = new MemberService(mockMemberDao);
        memberService.addMember("1");
        memberService.addMember("2");
        memberService.addMember("3");

        assertThat(memberService.findAllMembers().size()).isEqualTo(3);
    }

    @DisplayName("id로 멤버를 삭제한다.")
    @Test
    void deleteMember() {
        final MemberService memberService = new MemberService(mockMemberDao);
        final Long memberId = memberService.addMember("1");

        memberService.deleteMember(memberId);

        assertThatThrownBy(() -> memberService.findById(memberId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("멤버를 찾을 수 없습니다.");
    }
}