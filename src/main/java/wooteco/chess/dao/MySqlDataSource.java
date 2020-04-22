package wooteco.chess.dao;

public class MySqlDataSource extends AbstractDataSource {
	public MySqlDataSource() {
		super(new JdbcConfiguration.Builder()
				.server("localhost:13306")
				.database("chess_game")
				.option("?useSSL=false&serverTimezone=UTC")
				.userName("root")
				.password("root")
				.databaseDriver("com.mysql.cj.jdbc.Driver")
				.dataSourceName("mysql")
				.build());
	}
}
