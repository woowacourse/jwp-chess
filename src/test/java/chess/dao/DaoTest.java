package chess.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class DaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    void setUp() {
        jdbcTemplate.execute("drop table piece if exists");
        jdbcTemplate.execute("drop table turn if exists");
        jdbcTemplate.execute("drop table room if exists");
        jdbcTemplate.execute("CREATE TABLE room (\n" +
                "    id bigint not null auto_increment primary key,\n" +
                "    name varchar(30) not null,\n" +
                "    password varchar(20) not null)"
        );
    }
}
