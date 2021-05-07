package chess.dao.jdbctemplate;

import chess.dao.GameDao;
import chess.dao.dto.game.GameDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@DataJdbcTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@TestPropertySource("classpath:application-test.properties")
class RoomDaoTemplateTest {

    private final JdbcTemplate jdbcTemplate;
    private final GameDao gameDao;

    public RoomDaoTemplateTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        gameDao = new GameDaoTemplate(jdbcTemplate);

    }

    @BeforeEach
    void setUp() {
        List<Object[]> splitsGameInfos = Stream.of(
                "게임1,흰색유저1,1234",
                "게임2,흰색유저2,1234",
                "게임3,흰색유저3,1234"
        )
                .map(game -> game.split(","))
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate("INSERT INTO game(room_name, white_username, white_password) VALUES(?,?,?)", splitsGameInfos);
        jdbcTemplate.execute("COMMIT");
    }

    @Test
    @DisplayName("게임 저장된다.")
    void shouldSaveGame() {
        //given
        String roomName = "게임4";
        String whiteUsername = "흰색유저4";
        String whitePassword = "1234";

        //when
        Long newGameId = gameDao.save(new GameDto(roomName, whiteUsername, whitePassword));
        GameDto findGame = gameDao.findById(newGameId);

        //then
        assertThat(findGame).isNotNull();
        assertThat(findGame.getRoomName()).isEqualTo(roomName);
        assertThat(findGame.getWhiteUsername()).isEqualTo(whiteUsername);
        assertThat(findGame.getWhitePassword()).isEqualTo(whitePassword);
    }

    @Test
    @DisplayName("게임 id 로 게임을 검색할 수 있다.")
    void shouldFindGameById() {
        //given
        String roomName = "게임1";
        String whiteUsername = "흰색유저1";
        String whitePassword = "1234";
        Long id = 1L;

        //when
        GameDto findGame = gameDao.findById(id);

        //then
        assertThat(findGame.getRoomName()).isEqualTo(roomName);
        assertThat(findGame.getWhiteUsername()).isEqualTo(whiteUsername);
        assertThat(findGame.getWhitePassword()).isEqualTo(whitePassword);
    }
}
