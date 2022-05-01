package chess.dao;

import static chess.util.RandomCreationUtils.createUuid;
import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.entity.BoardPiece;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class JdbcBoardPieceDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JdbcBoardPieceDao jdbcBoardPieceDao;


    @AfterEach
    void tearDown() {
        jdbcBoardPieceDao.deleteAll();
    }

    @DisplayName("저장하고 게임을 찾는 로직 테스트")
    @Test
//    @Rollback(value = false)
    void save_and_find_game() {
        String uuid = createUuid();
        jdbcBoardPieceDao.setForeignKeyChecks(false);
        jdbcBoardPieceDao.save(uuid, Map.of("a2", "blackPawn"));
        List<BoardPiece> lastBoardPiece = jdbcBoardPieceDao.findLastBoardPiece(uuid);
        assertThat(lastBoardPiece.size()).isEqualTo(1);
    }

    @DisplayName("저장하고 게임을 찾는 로직 테스트")
    @Test
//    @Rollback(value = false)
    void save_and_find_game2() {
        String uuid = createUuid();
        jdbcBoardPieceDao.setForeignKeyChecks(false);
        jdbcBoardPieceDao.save(uuid, Map.of("a2", "blackPawn"));
        List<BoardPiece> lastBoardPiece = jdbcBoardPieceDao.findLastBoardPiece(uuid);
        assertThat(lastBoardPiece.size()).isEqualTo(1);
    }
}