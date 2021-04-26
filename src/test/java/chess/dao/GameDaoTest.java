package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.ChessGame;
import chess.domain.team.BlackTeam;
import chess.domain.team.WhiteTeam;
import java.sql.PreparedStatement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class GameDaoTest {
    private final GameDao gameDao;
    private final JdbcTemplate jdbcTemplate;

    public GameDaoTest(final GameDao gameDao, final JdbcTemplate jdbcTemplate) {
        this.gameDao = gameDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("DELETE FROM team;"
            + "ALTER TABLE team ALTER COLUMN team_id RESTART WITH 1;"
            + "DELETE FROM piece;"
            + "ALTER TABLE piece ALTER COLUMN piece_id RESTART WITH 1;"
            + "DELETE FROM room;"
            + "ALTER TABLE room ALTER COLUMN room_id RESTART WITH 1;"
            + "DELETE FROM game;"
            + "ALTER TABLE game ALTER COLUMN game_id RESTART WITH 1;");

        ChessGame chessGame = new ChessGame(new WhiteTeam(), new BlackTeam());
        String sql = "insert into game (is_end) values (?)";
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setBoolean(1, chessGame.isEnd());
            return preparedStatement;
        });

        chessGame.finish();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setBoolean(1, chessGame.isEnd());
            return preparedStatement;
        });
    }

    @Test
    @DisplayName("게임 생성 테스트")
    void create() {
        ChessGame chessGame = new ChessGame(new WhiteTeam(), new BlackTeam());
        assertThat(gameDao.create(chessGame)).isEqualTo(3);
    }

    @Test
    @DisplayName("끝난 게임 테스트")
    void isEnd() {
        assertThat(gameDao.isEnd(1)).isFalse();
        assertThat(gameDao.isEnd(2)).isTrue();
    }

    @Test
    @DisplayName("게임정보 업데이트")
    void update() {
        gameDao.update(1, true);
        assertThat(gameDao.isEnd(1)).isTrue();
        gameDao.update(1, false);
        assertThat(gameDao.isEnd(1)).isFalse();
    }
}