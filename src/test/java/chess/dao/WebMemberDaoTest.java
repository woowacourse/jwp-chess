package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.game.ChessBoard;
import chess.domain.member.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class WebMemberDaoTest {

    @Autowired
    private WebChessMemberDao dao;

    @Autowired
    private WebChessBoardDao boardDao;

    private int boardId;

    @BeforeEach
    void setup() {
        final ChessBoard board = boardDao.save(new ChessBoard("에덴파이팅~!"));
        this.boardId = board.getId();
    }

    @Test
    void save() {
        final Member member = dao.save("eden", boardId);
        assertThat(member.getName()).isEqualTo("eden");
    }

    @AfterEach
    void setDown() {
        boardDao.deleteAll();
    }
}
