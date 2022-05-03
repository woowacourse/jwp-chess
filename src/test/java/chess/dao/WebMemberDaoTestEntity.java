package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.domain.Game;
import chess.domain.game.ChessBoard;
import chess.domain.game.ChessBoardInitializer;
import chess.domain.pieces.Color;
import chess.entities.GameEntity;
import chess.entities.MemberEntity;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class WebMemberDaoTestEntity {

    @Autowired
    private ChessMemberDao dao;

    @Autowired
    private ChessBoardDao boardDao;

    private int boardId;

    @BeforeEach
    void setup() {
        final GameEntity board = boardDao.save(new GameEntity("에덴파이팅~!", "1111", new Game(new ChessBoard(new ChessBoardInitializer()), Color.WHITE)));
        this.boardId = board.getId();
        dao.saveAll(List.of(new MemberEntity("쿼리치"), new MemberEntity("코린")), boardId);
    }

    @Test
    void save() {
        final MemberEntity memberEntity = dao.save("eden", boardId);
        assertThat(memberEntity.getName()).isEqualTo("eden");
    }

    @Test
    void saveAll() {
        dao.saveAll(List.of(new MemberEntity("우테코"), new MemberEntity("백엔드")), boardId);
        List<MemberEntity> memberEntities = dao.getAllByBoardId(boardId);

        assertThat(memberEntities.size()).isEqualTo(4);
    }

    @Test
    void getAllByBoardId() {
        List<MemberEntity> memberEntities = dao.getAllByBoardId(boardId);
        assertAll(
                () -> assertThat(memberEntities.size()).isEqualTo(2),
                () -> assertThat(memberEntities.get(0).getName()).isEqualTo("쿼리치"),
                () -> assertThat(memberEntities.get(1).getName()).isEqualTo("코린")
        );

    }

    @AfterEach
    void setDown() {
        boardDao.deleteAll();
        dao.deleteAll();
    }
}
