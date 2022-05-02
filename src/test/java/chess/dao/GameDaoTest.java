package chess.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class GameDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private GameDao dao;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM game");
        jdbcTemplate.execute("ALTER TABLE game AUTO_INCREMENT = 1");
    }

    @Test
    @DisplayName("중복되는 이름이 있다면 true 반환")
    void isDuplicateNameWhenTrue() {
        jdbcTemplate.execute("INSERT INTO game(name,password) VALUES ('name','password')");

        assertThat(dao.isDuplicateName("name")).isTrue();
    }

    @Test
    @DisplayName("중복되는 이름이 없다면 false 반환")
    void isDuplicateNameWhenFalse() {
        jdbcTemplate.execute("INSERT INTO game(name,password) VALUES ('name','password')");

        assertThat(dao.isDuplicateName("name2")).isFalse();
    }

    @Test
    @DisplayName("게임 저장 확인")
    void saveGame() {
        int gameId = dao.saveGame("name", "password");

        assertThat(gameId).isEqualTo(1);
        assertThat(jdbcTemplate.queryForObject("SELECT name FROM game WHERE id=1", String.class)).isEqualTo("name");
        assertThat(jdbcTemplate.queryForObject("SELECT password FROM game WHERE id=1", String.class)).isEqualTo("password");
    }

    @Test
    @DisplayName("올바른 게임 목록을 반환하는지 확인")
    void findGameList() {
        jdbcTemplate.execute("INSERT INTO game(name,password) VALUES ('name','password')");
        jdbcTemplate.execute("INSERT INTO game(name,password) VALUES ('name2','password2')");

        Map<Integer, String> gameList = dao.findGameList();

        assertThat(gameList.get(1)).isEqualTo("name");
        assertThat(gameList.get(2)).isEqualTo("name2");
    }

    @Test
    @DisplayName("gameId에 따라 올바른 비밀번호를 반환하는지 확인")
    void findPasswordById() {
        jdbcTemplate.execute("INSERT INTO game(name,password) VALUES ('name','password')");

        assertThat(dao.findPasswordById(1)).isEqualTo("password");
    }

    @Test
    @DisplayName("gameId에 따라 삭제하는지 확인")
    void delete() {
        jdbcTemplate.execute("INSERT INTO game(name,password) VALUES ('name','password')");

        dao.delete(1);

        assertThat(jdbcTemplate.queryForObject("SELECT EXISTS (SELECT * FROM game WHERE id=1)", Boolean.class)).isFalse();
    }

}
