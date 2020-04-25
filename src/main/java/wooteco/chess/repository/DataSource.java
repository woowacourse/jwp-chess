package wooteco.chess.repository;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

@Component
public interface DataSource {
	Connection getConnection() throws SQLException;
}
