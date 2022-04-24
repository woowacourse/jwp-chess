package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import org.junit.jupiter.api.Test;

class MysqlDaoTest {

    @Test
    void connection() {
        Connection connection = MysqlDao.getConnection();
        assertThat(connection).isNotNull();
    }
}