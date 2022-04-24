package chess.web.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import org.junit.jupiter.api.Test;

class JdbcConnectorTest {

    @Test
    void getConnection() {
        final Connection connection = JdbcConnector.getConnection();
        assertThat(connection).isNotNull();
    }
}
