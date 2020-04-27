package wooteco.chess.database;

import java.sql.Connection;

public interface DataSource {

	Connection getConnection();

}
