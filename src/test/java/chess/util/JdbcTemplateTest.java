package chess.util;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dao.DataSourceImpl;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JdbcTemplateTest {

    @Test
    @DisplayName("Connection 확인")
    void getConnection() throws SQLException {
        Connection connection = new DataSourceImpl().getConnection();
        assertThat(connection).isNotNull();
        connection.close();
    }

}
