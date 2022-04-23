package chess.dao.springjdbc;

import static org.assertj.core.api.Assertions.assertThat;

import chess.service.dto.BoardDto;
import chess.service.dto.PieceWithSquareDto;
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
@Sql("classpath:static/init.sql")
class SpringBoardDaoTest {

    private SpringBoardDao springBoardDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        springBoardDao = new SpringBoardDao(jdbcTemplate);
    }

    @Test
    @DisplayName("initBoard : 보드를 정상적으로 초기화 하는지 확인")
    void initBoard() {
        initBoard("first");
        BoardDto boardDto = springBoardDao.getBoardByGameId(1);

        assertThat(boardDto.getPieces()).hasSize(64);
    }

    private void initBoard(String... names) {
        GameFactory.setUpGames(jdbcTemplate, names);
        for (int i = 1; i < names.length + 1; i++) {
            springBoardDao.initBoard(i);
        }
    }

    @Test
    @DisplayName("getBoardByGameId : 게임id로 보드를 정상적으로 가져오는 확인")
    void getBoardByGameId() {
        initBoard("first", "second");
        BoardDto boardDto = springBoardDao.getBoardByGameId(2);

        assertThat(boardDto.getPieces()).hasSize(64);
    }

    @Test
    @DisplayName("remove : 보드가 정상적으로 제거되는지 확인")
    void remove() {
        initBoard("first");
        springBoardDao.remove(1);

        assertThat(springBoardDao.getBoardByGameId(1).getPieces()).isEmpty();
    }

    @Test
    @DisplayName("update : 보드의 피스 정보들이 정상적으러 업데이트 되는지 확인")
    void update() {
        initBoard("first");
        PieceWithSquareDto piece = new PieceWithSquareDto("a4", "pawn", "white");
        springBoardDao.update(piece, 1);
        BoardDto board = springBoardDao.getBoardByGameId(1);

        assertThat(board.getPieces()).contains(piece);
    }
}
