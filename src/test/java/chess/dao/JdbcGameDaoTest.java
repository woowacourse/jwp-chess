package chess.dao;

import chess.entity.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

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
        Game game = new Game("라라라", "1234", "white", "playing");
        long id = jdbcGameDao.save(game);
        assertThat(id).isEqualTo(1);
    }
}
