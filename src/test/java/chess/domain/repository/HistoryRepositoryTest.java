package chess.domain.repository;

import chess.domain.dto.HistoryDto;
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
        jdbcTemplate.execute("DROP TABLE History IF EXISTS ");
        jdbcTemplate.execute("CREATE TABLE History (" +
                "                           history_id int not null auto_increment," +
                "                           name varchar(100) not null," +
                "                           is_end boolean not null default false," +
                "                           PRIMARY KEY (history_id))");
        jdbcTemplate.update("INSERT INTO History(name, is_end) VALUES(?, ?)", "joanne", "false");
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
        final List<HistoryDto> names = historyRepository.selectActive();
        assertThat(names.size()).isEqualTo(4);
    }

    @Test
    void updateEndState() {
        historyRepository.updateEndState(String.valueOf(1));
        final List<HistoryDto> names = historyRepository.selectActive();
        assertThat(names.size()).isEqualTo(0);
    }
}