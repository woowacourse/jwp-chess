package wooteco.chess.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;

public class JDBCConnectorTest {
	@DisplayName("JDBC Connection 테스트")
	@Test
	public void connection() {
		Connection con = JDBCConnector.getConnection();
		assertThat(con).isNotNull();
	}
}
