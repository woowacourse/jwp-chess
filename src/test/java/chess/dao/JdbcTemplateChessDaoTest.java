package chess.dao;

import chess.dao.dto.ChessGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("jdbcTemplateChessDao 테스트")
@JdbcTest
@TestPropertySource("classpath:application-test.properties")
class JdbcTemplateChessDaoTest {
    private final ChessGame sample = new ChessGame(1L, "WHITE", true, "RNBQKBNRPPPPPPPP................................pppppppprnbqkbnr");

    private JdbcTemplate jdbcTemplate;
    private JdbcTemplateChessDao jdbcTemplateChessDao;

    @Autowired
    public JdbcTemplateChessDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcTemplateChessDao = new JdbcTemplateChessDao(jdbcTemplate);
    }

    @DisplayName("저장 및 아이디로 chessGame을 찾아오는 기능 테스트")
    @Test
    void saveAndFindByIdTest() {
        //given
        //when
        long gameId = jdbcTemplateChessDao.save(sample);

        //then
        ChessGame chessGame = jdbcTemplateChessDao.findById(gameId).get();
        assertThat(chessGame.getNextTurn()).isEqualTo("WHITE");
    }

    @DisplayName("수정 기능 테스트")
    @Test
    void update() {
        //given
        String changedTurn = "BLACK";
        String expectedPieces = ".................RNBQKBNRPPPPPPPP...............pppppppprnbqkbnr";

        //when
        long gameId = jdbcTemplateChessDao.save(sample);
        jdbcTemplateChessDao.update(new ChessGame(gameId, changedTurn, true, expectedPieces));

        //then
        ChessGame chessGame = jdbcTemplateChessDao.findById(gameId).get();
        assertThat(chessGame.getPieces()).isEqualTo(expectedPieces);
        assertThat(chessGame.getNextTurn()).isEqualTo(changedTurn);
    }

    @DisplayName("끝나지 않은 게임을 찾아오는 기능 테스트")
    @Test
    void findAllOnRunning() {
        //given
        //when
        long firstGameId = jdbcTemplateChessDao.save(sample);
        long secondGameId = jdbcTemplateChessDao.save(sample);

        //then
        List<Long> onRunnings = jdbcTemplateChessDao.findAllOnRunning().stream()
                .map(ChessGame::getId)
                .collect(toList());

        assertThat(onRunnings).contains(firstGameId, secondGameId);
    }

    @DisplayName("삭제 기능 테스트")
    @Test
    void delete() {
        //given
        long gameId = jdbcTemplateChessDao.save(sample);

        //when
        jdbcTemplateChessDao.delete(gameId);
        //then
        assertThat(jdbcTemplateChessDao.findById(gameId));
    }
}