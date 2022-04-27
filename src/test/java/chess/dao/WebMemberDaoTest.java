package chess.dao;

import chess.domain.game.ChessBoard;
import chess.domain.member.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@Sql({"schema.sql"})
class WebMemberDaoTest {

    private final MemberDao<Member> dao;
    private final BoardDao<ChessBoard> boardDao;

    private int boardId;

    @Autowired
    WebMemberDaoTest(NamedParameterJdbcTemplate jdbcTemplate) {
        dao = new WebChessMemberDao(jdbcTemplate);
        boardDao = new WebChessBoardDao(dao, jdbcTemplate);
    }

    @BeforeEach
    void setup() {
        final ChessBoard board = boardDao.save(new ChessBoard("에덴파이팅~!", "1234"));
        this.boardId = board.getId();
        dao.saveAll(List.of(new Member("쿼리치"), new Member("코린")), boardId);
    }

    @Test
    void save() {
        final Member member = dao.save("eden", boardId);
        assertThat(member.getName()).isEqualTo("eden");
    }

    @Test
    void saveAll() {
        dao.saveAll(List.of(new Member("우테코"), new Member("백엔드")), boardId);
        List<Member> members = dao.getAllByBoardId(boardId);

        assertThat(members.size()).isEqualTo(4);
    }

    @Test
    void getAllByBoardId() {
        List<Member> members = dao.getAllByBoardId(boardId);
        assertAll(
                () -> assertThat(members.size()).isEqualTo(2),
                () -> assertThat(members.get(0).getName()).isEqualTo("쿼리치"),
                () -> assertThat(members.get(1).getName()).isEqualTo("코린")
        );
    }

    @AfterEach
    void setDown() {
        boardDao.deleteAll();
        dao.deleteAll();
    }
}
