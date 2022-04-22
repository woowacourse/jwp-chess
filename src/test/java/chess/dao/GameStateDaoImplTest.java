package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class GameStateDaoImplTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private GameStateDaoImpl gameStateDaoImpl;

    @BeforeEach
    void setUp() {
        gameStateDaoImpl = new GameStateDaoImpl(jdbcTemplate);
        jdbcTemplate.execute("DROP TABLE game IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE game(id SERIAL, state varchar(7), turn varchar(5))");
    }

    @Test
    @DisplayName("턴 정보를 DB에 저장한다.")
    void saveTurn() {
        //given
        gameStateDaoImpl.saveTurn("WHITE");
        //when
        final String actual = gameStateDaoImpl.getTurn();
        //then
        assertThat(actual).isEqualTo("WHITE");
    }

    @Test
    @DisplayName("게임 상태를 DB에 저장한다.")
    void saveState() {
        //given
        gameStateDaoImpl.saveState("playing");
        //when
        final String actual = gameStateDaoImpl.getGameState();
        //then
        assertThat(actual).isEqualTo("playing");
    }
}