package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.piece.Team;
import chess.dto.GameDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql(value = {"../../../resources/schema.sql"})
class GameJdbcDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private GameDao gameDao;
    private int gameId = 1;

    @BeforeEach
    void setup() {
        gameDao = new GameJdbcDao(jdbcTemplate, new BCryptPasswordEncoder());
    }

    @Test
    @DisplayName("아이디로 게임 정보를 찾는다.")
    void findById() {
        GameDto gameDto = gameDao.findGames().get(0);

        assertThat(gameDto.getRoomName()).isEqualTo("test");
    }

    @Test
    @DisplayName("게임의 상태를 업데이트한다.")
    void update() {
        gameDao.update(Team.BLACK.name(), gameId);
        GameDto gameDto = gameDao.findGames().get(0);

        assertThat(gameDto.getState()).isEqualTo(Team.BLACK.name());
    }
}
