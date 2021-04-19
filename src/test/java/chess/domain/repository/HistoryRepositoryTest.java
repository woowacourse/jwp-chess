package chess.domain.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class HistoryRepositoryTest {
    private HistoryRepository historyRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        historyRepository = new HistoryRepository(jdbcTemplate);

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
        historyRepository.insert("whybe");
    }

    @Test
    void findIdByName() {
        final Optional<Integer> id = historyRepository.findIdByName("joanne");
        assertThat(id).isPresent();
    }

    @Test
    void delete() {
        final int id = historyRepository.delete("joanne");
        assertThat(id).isEqualTo(1);
    }

    @Test
    void selectActive() {
        historyRepository.insert("jino");
        historyRepository.insert("pobi");
        historyRepository.insert("jason");
        final List<String> names = historyRepository.selectActive();
        assertThat(names).contains("joanne", "jino", "pobi", "jason");
    }

    @Test
    void updateEndState() {
        historyRepository.updateEndState(String.valueOf(1));
        final List<String> names = historyRepository.selectActive();
        assertThat(names.size()).isEqualTo(0);
    }
}