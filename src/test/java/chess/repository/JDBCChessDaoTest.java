package chess.repository;

import chess.dao.ChessDao;
import chess.entity.Chess;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

@SpringBootTest
class JDBCChessDaoTest {

    @Autowired
    private ChessDao chessDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS chess");
        String query = "CREATE TABLE IF NOT EXISTS chess ( " +
                "chess_id VARCHAR(36) NOT NULL," +
                "name VARCHAR(64) NOT NULL," +
                "winner_color VARCHAR(64) NOT NULL," +
                "is_running boolean not null default false ," +
                "created_date TIMESTAMP(6)," +
                "PRIMARY KEY (chess_id)" +
                ");";
        jdbcTemplate.execute(query);
    }

    @DisplayName("저장 테스트")
    @Test
    void name() {
        String test = "테스트";
        Chess chess = new Chess(test);

        chessDao.save(chess);

        Optional<Chess> findChess = chessDao.findByName(test);

        Assertions.assertThat(chess.getName()).isEqualTo(findChess.get().getName());
    }
}