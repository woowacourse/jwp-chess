package chess.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@Sql("classpath:init.sql")
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("정상적으로 멤버가 등록되는지 확인한다.")
    void addMember() {
        assertThat(memberService.addMember("썬")).isEqualTo(1L);
    }

    @Test
    @DisplayName("모든 멤버가 불러와지는지 확인한다.")
    void findAllMembers() {
        memberService.addMember("썬");
        memberService.addMember("포비");
        memberService.addMember("제이슨");
        memberService.addMember("네오");

        assertThat(memberService.findAllMembers().size()).isEqualTo(4);
    }

    @Test
    @DisplayName("정상적으로 멤버가 삭제되는지 확인한다.")
    void deleteMember() {
        final Long memberId = memberService.addMember("썬");
        memberService.deleteMember(memberId);

        assertThat(memberService.findAllMembers()).isEmpty();
    }
}
