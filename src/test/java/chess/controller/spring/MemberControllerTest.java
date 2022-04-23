package chess.controller.spring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import chess.domain.Member;
import chess.service.MemberService;
import io.restassured.RestAssured;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("멤버의 이름을 담아서 멤버 생성을 요청하면 멤버가 생성된다.")
    @Test
    void addMember() {
        final Member member = new Member("알렉스");
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(member)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("멤버의 아이디로 멤버를 요청하면 해당 멤버의 id와 이름을 응답한다.")
    @Test
    void getMember() {
        final Long memberId = memberService.addMember("알렉스");

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/" + memberId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("name", notNullValue());
    }

    @DisplayName("멤버 id를 주면서 멤버 삭제를 요청하면 해당 멤버가 삭제된다.")
    @Test
    void deleteMember() {
        final Long memberId = memberService.addMember("알렉스");

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/members/" + memberId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        final List<Member> members = memberService.findAllMembers();
        assertThat(members.size()).isEqualTo(0);
    }
}