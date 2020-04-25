package wooteco.chess.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PrepareStatementSetter {
	void set(PreparedStatement preparedStatement) throws SQLException;
}
