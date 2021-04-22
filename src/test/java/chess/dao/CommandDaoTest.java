package chess.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CommandDaoTest {
    @Autowired
    private CommandDao commandDAO;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setup() {
        jdbcTemplate.execute("DROP TABLE command IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE customers(" +
                "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");

        List<Object[]> splitUpNames = Stream.of("a7 a5", "b2 b3", "a5 a4", "b3 a4")
                                            .map(position -> position.split(" "))
                                            .collect(Collectors.toList());

        jdbcTemplate.batchUpdate("INSERT INTO command(room_id, move_from, move_to) VALUES (1L, ?,?)", splitUpNames);
    }

    @Test
    void insert() {
        commandDAO.insert(2L, "c2", "c4");
    }
}