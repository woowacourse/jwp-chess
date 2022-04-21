package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class GameDaoTest {

    private static final String TEST_TABLE = "game_test";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private GameDao dao;

    @BeforeEach
    void setUp() {
        dao = new GameDao(namedParameterJdbcTemplate) {
            @Override
            protected String addTable(String sql) {
                return String.format(sql, TEST_TABLE);
            }
        };
        jdbcTemplate.execute("DROP TABLE game_test IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE game_test("
                + "id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
                + "state ENUM('RUNNING', 'OVER') NOT NULL)");

        jdbcTemplate.execute("INSERT INTO game_test (id, state) "
                + "VALUES (1, 'RUNNING'), (2, 'OVER')");
    }

    @Test
    void saveAndGetGeneratedId_메서드는_저장한_데이터의_id값_반환() {
        int actual = dao.saveAndGetGeneratedId();

        assertThat(actual).isEqualTo(3);
    }

    @Test
    void finishGame_메서드로_게임의_상태를_OVER로_변경가능() {
        dao.finishGame(1);

        String actual = jdbcTemplate.queryForObject(
                "SELECT state FROM game_test WHERE id = 1", String.class);

        assertThat(actual).isEqualTo("OVER");
    }

    @Test
    void checkById_메서드로_id에_해당되는_데이터_존재여부_확인가능() {
        boolean actual = dao.checkById(1);

        assertThat(actual).isTrue();
    }

    @Test
    void checkById_메서드에서_id에_해당되는_데이터가_없으면_거짓_반환() {
        boolean actual = dao.checkById(9999);

        assertThat(actual).isFalse();
    }

    @Test
    void countAll_메서드로_여태까지_저장된_모든_데이터의_개수_조회가능() {
        int actual = dao.countAll();

        assertThat(actual).isEqualTo(2);
    }

    @Test
    void countByState_메서드로_특정_state에_해당되는_데이터의_개수_조회가능() {
        int actual = dao.countByState(GameState.OVER);

        assertThat(actual).isEqualTo(1);
    }
}
