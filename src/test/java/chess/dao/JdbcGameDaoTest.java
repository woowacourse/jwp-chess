package chess.dao;

import chess.dao.dto.GameDto;
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
        GameDto deleteGameDto = new GameDto(id, "1234");
        jdbcGameDao.remove(deleteGameDto);

        // then
        assertThatThrownBy(() -> jdbcGameDao.find(id, "1234"))
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
        GameDto selectedGameDto = jdbcGameDao.find(id, password);

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
}
