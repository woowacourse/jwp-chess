package chess.dao.springjdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.entity.PieceEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
@Sql("classpath:schema-test.sql")
class SpringPieceDaoTest {

    private SpringPieceDao springPieceDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        springPieceDao = new SpringPieceDao(jdbcTemplate);
    }

    @Test
    @DisplayName("initBoard : 보드를 정상적으로 초기화 하는지 확인")
    void initBoard() {
        GameFactory.setUpGames(jdbcTemplate, "first");
        springPieceDao.initBoard(1);
        List<PieceEntity> pieces = springPieceDao.getBoardByGameId(1);

        assertThat(pieces).hasSize(64);
    }

    @Test
    @DisplayName("getBoardByGameId : 게임id로 보드를 정상적으로 가져오는 확인")
    void getBoardByGameId() {
        initBoard("first", "second");
        List<PieceEntity> pieces = springPieceDao.getBoardByGameId(2);

        assertThat(pieces).hasSize(64);
    }

    private void initBoard(String... names) {
        GameFactory.setUpGames(jdbcTemplate, names);
        for (int i = 1; i < names.length + 1; i++) {
            springPieceDao.initBoard(i);
        }
    }

    @Test
    @DisplayName("remove : 보드가 정상적으로 제거되는지 확인")
    void remove() {
        initBoard("first");
        springPieceDao.remove(1);

        assertThat(springPieceDao.getBoardByGameId(1)).isEmpty();
    }

    @Test
    @DisplayName("update : 보드의 피스 정보들이 정상적으러 업데이트 되는지 확인")
    void update() {
        initBoard("first");
        PieceEntity piece = new PieceEntity("a4", "pawn", "white");
        int affectedRows = springPieceDao.update(piece, 1);
        List<PieceEntity> pieces = springPieceDao.getBoardByGameId(1);
        assertAll(() -> {
            assertThat(affectedRows).isEqualTo(1);
            assertThat(pieces).contains(piece);
        });
    }
}
