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
//        jdbcTemplate.execute("DROP TABLE IF EXISTS command ");
//        jdbcTemplate.execute("CREATE TABLE command(" +
//                "game_id SERIAL, move_from VARCHAR(255), move_to VARCHAR(255))");
//
//        List<Object[]> splitUpNames = Stream.of("1 a7 a5", "2 b2 b3", "3 a5 a4", "4 b3 a4")
//                                            .map(position -> position.split(" "))
//                                            .collect(Collectors.toList());
//
//        jdbcTemplate.batchUpdate("INSERT INTO command(move_from, move_to) VALUES (?, ?)", splitUpNames);
    }

    @Test
    void insert() {
//        commandDAO.insert(5L, "c2", "c4");
    }
}