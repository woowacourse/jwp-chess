package chess.dao;

import chess.dao.dto.GameDto;
import chess.domain.GameStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@Sql("classpath:init.sql")
public class JdbcGameDaoTest {

    private JdbcGameDao jdbcGameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcGameDao = new JdbcGameDao(jdbcTemplate);
    }

    @Test
    @DisplayName("게임 데이터 저장")
    void save() {
        // given
        GameDto gameDto = new GameDto("라라라", "1234", "white", "playing");

        // when
        long id = jdbcGameDao.save(gameDto);

        // then
        assertThat(id).isEqualTo(1);
    }

    @Test
    @DisplayName("게임 데이터 삭제")
    void remove() {
        // given
        GameDto gameDto = new GameDto("라라라", "1234", "white", "playing");
        long id = jdbcGameDao.save(gameDto);

        // when
        jdbcGameDao.removeById(id);

        // then
        assertThatThrownBy(() -> jdbcGameDao.findById(id))
                .isInstanceOfAny(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("게임 데이터 저장")
    void find() {
        // given
        String title = "라라라";
        String password = "1234";
        String turn = "white";
        String status = "playing";
        GameDto gameDto = new GameDto(title, password, turn, status);
        long id = jdbcGameDao.save(gameDto);

        // when
        GameDto selectedGameDto = jdbcGameDao.findById(id);

        // then
        assertAll(
                () -> assertThat(selectedGameDto).isNotNull(),
                () -> assertThat(selectedGameDto.getId()).isEqualTo(id),
                () -> assertThat(selectedGameDto.getTitle()).isEqualTo(title),
                () -> assertThat(selectedGameDto.getTurn()).isEqualTo(turn),
                () -> assertThat(selectedGameDto.getStatus()).isEqualTo(status)
        );
    }

    @Test
    @DisplayName("모든 게임 데이터 저장")
    void findAll() {
        // given
        GameDto gameDto1 = new GameDto("라라라", "1234", "white", "playing");
        GameDto gameDto2 = new GameDto("룰룰루", "222", "white", "playing");
        jdbcGameDao.save(gameDto1);
        jdbcGameDao.save(gameDto2);

        // when
        List<GameDto> gameDtos = jdbcGameDao.findAll();

        // then
        assertThat(gameDtos.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("게임 정보 수정")
    void update() {
        // given
        GameDto gameDto = new GameDto("라라라", "1234", "white", "playing");
        long id = jdbcGameDao.save(gameDto);

        // when
        GameDto updatedGameDto = new GameDto(id, "라라라", "1234", "black", "end");
        jdbcGameDao.updateGame(updatedGameDto);

        // then
        assertAll(
                () -> assertThat(jdbcGameDao.findById(id).getTurn()).isEqualTo("black"),
                () -> assertThat(jdbcGameDao.findById(id).getStatus()).isEqualTo("end")
        );
    }

    @Test
    @DisplayName("게임 상태 업데이트")
    void updateStatus() {
        // given
        GameDto gameDto = new GameDto("라라라", "1234", "white", "playing");
        long id = jdbcGameDao.save(gameDto);

        // when
        GameStatus gameStatus = GameStatus.FINISHED;
        jdbcGameDao.updateStatus(id, gameStatus);

        // then
        assertThat(jdbcGameDao.findById(id).getStatus()).isEqualTo(gameStatus.getName());
    }
}
