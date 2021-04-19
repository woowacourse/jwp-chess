package chess.domain.repository;

import chess.domain.dto.CommandDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class CommandRepositoryTest {
    private CommandRepository commandRepository;
    private HistoryRepository historyRepository;
    private String keyId;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        keyId = String.valueOf(1);
        historyRepository = new HistoryRepository(jdbcTemplate);
        jdbcTemplate.update("INSERT INTO History(name, is_end) VALUES(?, ?)", "joanne", "false");

        commandRepository = new CommandRepository(jdbcTemplate);
        jdbcTemplate.update("INSERT INTO Command(data, history_id) VALUES(?, ?)", "move a1 a2", "1");
        jdbcTemplate.update("INSERT INTO Command(data, history_id) VALUES(?, ?)", "move a7 a6", "1");
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
        CommandDto commandDto = new CommandDto("move b1 b2");
        commandRepository.insert(commandDto, 1);
        final List<CommandDto> commands = commandRepository.selectAllCommands(String.valueOf(1));
        assertThat(commands.size()).isEqualTo(3);
    }

    @Test
    void selectAllCommands() {
        List<CommandDto> commandDtos = commandRepository.selectAllCommands(keyId);
        assertThat(commandDtos.get(0).data()).isEqualTo("move a1 a2");
        assertThat(commandDtos.get(1).data()).isEqualTo("move a7 a6");
    }
}