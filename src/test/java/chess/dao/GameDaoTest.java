package chess.dao;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class GameDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private GameDao dao;

    @AfterEach()
    void clear() {
        jdbcTemplate.execute("DELETE FROM game");
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
        int id = insertAndGetId();

        assertThat(jdbcTemplate.queryForObject("SELECT name FROM game WHERE id=?", String.class, id)).isEqualTo("name");
        assertThat(jdbcTemplate.queryForObject("SELECT password FROM game WHERE id=?", String.class, id)).isEqualTo("password");
    }

    @Test
    @DisplayName("올바른 게임 목록을 반환하는지 확인")
    void findGameList() {
        int id1 = insertAndGetId("name","password");
        int id2 = insertAndGetId("name2","password2");

        Map<Integer, String> gameList = dao.findGameList();

        assertThat(gameList.get(id1)).isEqualTo("name");
        assertThat(gameList.get(id2)).isEqualTo("name2");
    }

    @Test
    @DisplayName("gameId에 따라 올바른 비밀번호를 반환하는지 확인")
    void findPasswordById() {
        int id = insertAndGetId();

        assertThat(dao.findPasswordById(id)).isEqualTo("password");
    }

    @Test
    @DisplayName("gameId에 따라 삭제하는지 확인")
    void delete() {
        int id = insertAndGetId();

        dao.delete(id);

        assertThat(jdbcTemplate.queryForObject("SELECT EXISTS (SELECT * FROM game WHERE id=?)", Boolean.class, id)).isFalse();
    }

    private int insertAndGetId(String name, String password) {
        final String sql = "INSERT INTO game(name,password) VALUES (?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, name);
            ps.setString(2, password);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    private int insertAndGetId() {
        return insertAndGetId("name", "password");
    }

}
