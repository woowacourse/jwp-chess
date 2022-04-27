package chess.dao;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import chess.dto.GameDto;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class JdbcTemplateGameDaoTest {

    private JdbcTemplateGameDao jdbcTemplateGameDao;
    private int id;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void create() {
        jdbcTemplateGameDao = new JdbcTemplateGameDao(jdbcTemplate);
        id = jdbcTemplateGameDao.create("asdf", "1234");
    }

    @Test
    @DisplayName("roomTitle과 passWord를 저장한다.")
    void find() {
        List<GameDto> gameDtos = jdbcTemplateGameDao.find();
        GameDto gameDto = gameDtos.get(0);
        assertAll(
                () -> assertEquals(gameDto.getRoomTitle(), "asdf"),
                () -> assertEquals(gameDto.getPassword(), "1234")
        );
    }

    @AfterEach
    void delete() {
        jdbcTemplateGameDao.delete(id);
    }
}
