package chess.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
@Sql("classpath:schema.sql")
public class RoomDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private RoomDao roomDao;

    @BeforeEach
    void setUp() {
        roomDao = new RoomDao(jdbcTemplate, namedParameterJdbcTemplate);
    }
}
