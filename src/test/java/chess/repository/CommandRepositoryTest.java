package chess.repository;

import chess.dto.CommandDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@TestPropertySource("classpath:application-test.properties")
class CommandRepositoryTest {
    private CommandRepository commandRepository;
    private String keyId;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        keyId = String.valueOf(1);
        jdbcTemplate.execute("DROP TABLE Command IF EXISTS ");
        jdbcTemplate.execute("DROP TABLE History IF EXISTS ");
        jdbcTemplate.execute("CREATE TABLE History (" +
                "                           history_id int not null auto_increment," +
                "                           name varchar(100) not null," +
                "                           is_end boolean not null default false," +
                "                           PRIMARY KEY (history_id))");
        jdbcTemplate.update("INSERT INTO History(name, is_end) VALUES(?, ?)", "joanne", "false");

        commandRepository = new CommandRepository(jdbcTemplate);
        jdbcTemplate.execute("CREATE TABLE Command (" +
                "                           command_id int not null auto_increment," +
                "                           data text not null," +
                "                           history_id int not null," +
                "                           PRIMARY KEY (command_id)," +
                "                           FOREIGN KEY (history_id) REFERENCES History(history_id))");
        jdbcTemplate.update("INSERT INTO Command(data, history_id) VALUES(?, ?)", "move a1 a2", "1");
        jdbcTemplate.update("INSERT INTO Command(data, history_id) VALUES(?, ?)", "move a7 a6", "1");
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