package chess.domain.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class HistoryDaoImplTest {

    private HistoryDaoImpl historyDaoImpl;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        historyDaoImpl = new HistoryDaoImpl(jdbcTemplate);

        jdbcTemplate.update("INSERT INTO History(name, is_end) VALUES(?, ?)", "joanne", "false");
    }

    @AfterEach
    void reset() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        jdbcTemplate.update("TRUNCATE TABLE Command");
        jdbcTemplate.update("TRUNCATE TABLE History");
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
        jdbcTemplate.update("ALTER TABLE Command ALTER COLUMN command_id RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE History ALTER COLUMN history_id RESTART WITH 1");
    }

    @Test
    void insert() {
        historyDaoImpl.insert("whybe");
    }

    @Test
    void findIdByName() {
        final Optional<Integer> id = historyDaoImpl.findIdByName("joanne");
        assertThat(id).isPresent();
    }

    @Test
    void delete() {
        final int id = historyDaoImpl.delete("joanne");
        assertThat(id).isEqualTo(1);
    }

    @Test
    void selectActive() {
        historyDaoImpl.insert("hue");
        historyDaoImpl.insert("pobi");
        historyDaoImpl.insert("jason");
        final List<String> names = historyDaoImpl.selectActive();
        assertThat(names).contains("joanne", "hue", "pobi", "jason");
    }

    @Test
    void updateEndState() {
        historyDaoImpl.updateEndState(String.valueOf(1));
        final List<String> names = historyDaoImpl.selectActive();
        assertThat(names.size()).isEqualTo(0);
    }
}