package chess.dao;

import chess.controller.dto.GameDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class GameDaoTest {
    @Autowired
    private GameDao gameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @BeforeEach
    public void setup() {
        jdbcTemplate.update("INSERT INTO game (game_name) VALUES (?)", "test");
    }

    @Test
    @DisplayName("Id로 존재하는 값 검색")
    void findById() {
        GameDto gameDto = gameDao.findById(1L);
        assertThat(gameDto.getGameName()).isEqualTo("test");
    }


    @Test
    @DisplayName("Id로 존재하지 않는 값 검색")
    void findByIdNotExist() {
        assertThatThrownBy(() -> gameDao.findById(10L)).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("정상 생성 확인")
    void insert() {
        Long id = gameDao.insert("new");
        GameDto gameDto = gameDao.findById(id);
        assertThat(gameDto.getGameName()).isEqualTo("new");
    }

    @Test
    @DisplayName("전체 Game 쿼리 확인")
    void selectAll() {
        gameDao.insert("new1");
        gameDao.insert("new2");
        gameDao.insert("new3");
        gameDao.insert("new4");
        assertThat(gameDao.selectAll()).hasSize(5);
    }

    @Test
    @DisplayName("정상 삭제 확인")
    void delete() {
        gameDao.delete(1L);
        assertThat(gameDao.selectAll()).hasSize(0);
    }

}