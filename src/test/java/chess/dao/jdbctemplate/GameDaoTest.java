package chess.dao.jdbctemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import chess.domain.state.BlackRunning;
import chess.domain.state.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@TestPropertySource("classpath:application-test.properties")
class GameDaoTest {

    @Autowired
    private GameDao gameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE board IF EXISTS");
        jdbcTemplate.execute("DROP TABLE game IF EXISTS");
        jdbcTemplate.execute("create table game\n"
                + "(\n"
                + "    id    int auto_increment\n"
                + "        primary key,\n"
                + "    state varchar(20) not null\n"
                + ");");
        jdbcTemplate.execute("create table board\n"
                + "(\n"
                + "    position varchar(5)  not null,\n"
                + "    symbol   varchar(10) not null,\n"
                + "    color    varchar(10) not null,\n"
                + "    game_id  int         not null,\n"
                + "    primary key (position, game_id),\n"
                + "    constraint board_game_id_fk\n"
                + "        foreign key (game_id) references game (id)\n"
                + "            on update cascade on delete cascade\n"
                + ");");
        jdbcTemplate.update("insert into game(state) values (?)", "WhiteRunning");
    }

    @Test
    @DisplayName("게임을 생성할 수 있다.")
    void saveGame() {
        assertThatCode(() -> {
            gameDao.save("WhiteRunning");
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("게임의 상태를 조회할 수 있다.")
    void findState() {
        assertThat(gameDao.findState()).isInstanceOf(State.class);
    }

    @Test
    @DisplayName("게임의 일련번호를 조회할 수 있다.")
    void findId() {
        assertThat(gameDao.findId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("게임의 상태를 업데이트할 수 있다.")
    void updateGameByState() {
        gameDao.update("BlackRunning", 1L);
        assertThat(gameDao.findState()).isInstanceOf(BlackRunning.class);
    }

    @Test
    @DisplayName("게임을 제거할 수 있다.")
    void deleteGame() {
        assertThat(gameDao.delete()).isEqualTo(1L);
    }
}
