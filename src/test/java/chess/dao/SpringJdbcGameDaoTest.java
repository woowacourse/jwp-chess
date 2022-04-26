package chess.dao;

import chess.dto.GameDto;
import chess.dto.GameStatusDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@Sql("classpath:init.sql")
public class SpringJdbcGameDaoTest {

    private SpringJdbcGameDao springJdbcGameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        springJdbcGameDao = new SpringJdbcGameDao(jdbcTemplate);
    }

    @Test
    @DisplayName("전체 게임 데이터 삭제")
    void removeAll() {
        GameDto gameDto = new GameDto("white", "playing");
        springJdbcGameDao.saveGame(gameDto);

        springJdbcGameDao.removeAll();

        assertThat(getGameCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("게임 정보 저장")
    void save() {
        GameDto gameDto = new GameDto("white", "playing");
        springJdbcGameDao.saveGame(gameDto);

        assertThat(getGameCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("게임 정보 수정")
    void update() {
        GameDto gameDto = new GameDto("white", "playing");
        springJdbcGameDao.saveGame(gameDto);

        GameDto updatedGameDto = new GameDto("black", "end");
        springJdbcGameDao.updateGame(updatedGameDto);

        assertAll(
                () -> assertThat(springJdbcGameDao.findGame().getTurn()).isEqualTo("black"),
                () -> assertThat(springJdbcGameDao.findGame().getStatus()).isEqualTo("end")
        );
    }

    @Test
    @DisplayName("게임 상태 업데이트")
    void updateStatus() {
        GameDto gameDto = new GameDto("white", "playing");
        springJdbcGameDao.saveGame(gameDto);

        GameStatusDto gameStatusDto = GameStatusDto.FINISHED;
        springJdbcGameDao.updateStatus(gameStatusDto);

        assertThat(springJdbcGameDao.findGame().getStatus()).isEqualTo(gameStatusDto.getName());
    }

    @Test
    @DisplayName("게임 정보 조회")
    void find() {
        GameDto gameDto = new GameDto("white", "playing");
        springJdbcGameDao.saveGame(gameDto);

        assertAll(
                () -> assertThat(springJdbcGameDao.findGame().getStatus()).isEqualTo("playing"),
                () -> assertThat(springJdbcGameDao.findGame().getTurn()).isEqualTo("white")
        );
    }

    private Integer getGameCount() {
        return jdbcTemplate.queryForObject("select count(*) from game", Integer.class);
    }
}
