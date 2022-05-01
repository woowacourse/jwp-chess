package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.entity.BoardPiece;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql(statements = "insert into games values ('test-game-id', 'Black Team', now())")
class JdbcBoardPieceDaoTest {

    @Autowired
    private JdbcBoardPieceDao jdbcBoardPieceDao;

    private String gameId = "test-game-id";

    @DisplayName("저장하고 게임을 찾는 로직 테스트")
    @Test
    void save_and_find_game() {
        jdbcBoardPieceDao.save(gameId, Map.of("a2", "blackPawn"));
        List<BoardPiece> lastBoardPiece = jdbcBoardPieceDao.findLastBoardPiece(gameId);
        assertThat(lastBoardPiece.size()).isEqualTo(1);
    }
}